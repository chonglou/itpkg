#!/bin/sh

groupadd -g 5000 vmail
useradd -u 5000 -g vmail -s /usr/bin/nologin -d /home/vmail -m vmail

#------------- mysql ---------------------
#root_mysql_passwd=`pwgen 32 1`
root_mysql_passwd=123456
mail_mysql_passwd=`pwgen 32 1`

echo "Setup database"

SECURE_MYSQL=$(expect -c "

set timeout 10
spawn mysql_secure_installation

expect \"Enter current password for root (enter for none):\"
send \"\r\"

expect \"Change the root password?\"
send \"Y\r\"

expect \"New password:\"
send \"$root_mysql_passwd\r\"

expect \"Re-enter new password:\"
send \"$root_mysql_passwd\r\"

expect \"Remove anonymous users?\"
send \"y\r\"

expect \"Disallow root login remotely?\"
send \"y\r\"

expect \"Remove test database and access to it?\"
send \"y\r\"

expect \"Reload privilege tables now?\"
send \"y\r\"

expect eof
")
 
cd /mnt
echo "CREATE DATABASE mail;\nCREATE USER 'mail'@'localhost' IDENTIFIED BY '$mail_mysql_passwd';\nGRANT ALL ON mail.* TO 'mail'@'localhost';\n" > init.sql

cat >> init.sql <<EOF
USE mail;
CREATE TABLE `virtual_domains` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `virtual_users` (
  `id` int(11) NOT NULL auto_increment,
  `domain_id` int(11) NOT NULL,
  `password` varchar(106) NOT NULL,
  `email` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  FOREIGN KEY (domain_id) REFERENCES virtual_domains(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `virtual_aliases` (
  `id` int(11) NOT NULL auto_increment,
  `domain_id` int(11) NOT NULL,
  `source` varchar(100) NOT NULL,
  `destination` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (domain_id) REFERENCES virtual_domains(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `virtual_domains` (`id`, `name`) VALUES ('1', 'test.com')
INSERT INTO `virtual_users` (`domain_id`, `password` , `email`) VALUES ('1', ENCRYPT('changeme', CONCAT('$6$', SUBSTRING(SHA(RAND()), -16))), 'user1@test.com'), ('1', ENCRYPT('changeme', CONCAT('$6$', SUBSTRING(SHA(RAND()), -16))), 'user2@test.com');
INSERT INTO `virtual_aliases` (`domain_id`, `source`, `destination`) VALUES ( '1', 'alias1@test.com', 'user1@test.com');

EOF

MYSQL_PWD=$root_mysql_passwd mysql -u root < init.sql

#------------- openssl ---------------------
echo "Generate ssl certs"
cd /etc/ssl/private/
openssl req -new -x509 -nodes -newkey rsa:4096 -keyout vmail.key -out vmail.crt -days 3560 -subj '/CN=itpkg.com/O=IT-PACKAGE LTD./C=US'
chmod 400 vmail.key
chmod 444 vmail.crt

#------------- postfix ---------------------
echo "Setup postfix"

cd /etc/postfix
cat > main.cf <<EOF
smtpd_banner = $myhostname ESMTP $mail_name (Linux)
biff = no
append_dot_mydomain = no
readme_directory = no

relay_domains = $mydestination
virtual_alias_maps = proxy:mysql:/etc/postfix/virtual_alias_maps.cf
virtual_mailbox_domains = proxy:mysql:/etc/postfix/virtual_mailbox_domains.cf
virtual_mailbox_maps = proxy:mysql:/etc/postfix/virtual_mailbox_maps.cf
virtual_mailbox_base = /home/vmail
virtual_mailbox_limit = 512000000
virtual_minimum_uid = 5000
virtual_transport = virtual
virtual_uid_maps = static:5000
virtual_gid_maps = static:5000
local_transport = virtual
local_recipient_maps = $virtual_mailbox_maps
transport_maps = hash:/etc/postfix/transport

smtpd_sasl_auth_enable = yes
smtpd_sasl_type = dovecot
smtpd_sasl_path = private/auth
smtpd_recipient_restrictions = permit_mynetworks, permit_sasl_authenticated, reject_unauth_destination
smtpd_relay_restrictions = permit_mynetworks, permit_sasl_authenticated, reject_unauth_destination
smtpd_sasl_security_options = noanonymous
smtpd_sasl_tls_security_options = $smtpd_sasl_security_options
smtpd_use_tls = yes
smtpd_tls_security_level = may
smtpd_tls_auth_only = yes
smtpd_tls_received_header = yes
smtpd_tls_cert_file = /etc/ssl/private/vmail.crt
smtpd_tls_key_file = /etc/ssl/private/vmail.key
smtpd_sasl_local_domain = $mydomain
broken_sasl_auth_clients = yes
smtpd_tls_loglevel = 1

myhostname = mail.itpkg.com
alias_maps = hash:/etc/aliases
alias_database = hash:/etc/aliases
myorigin = /etc/mailname
mydestination = localhost
relayhost =
mynetworks = 127.0.0.0/8 [::ffff:127.0.0.0]/104 [::1]/128
mailbox_size_limit = 0
recipient_delimiter = +
inet_interfaces = all

EOF

cat > master.cf <<EOF
smtp      inet  n       -       -       -       -       smtpd
submission inet n       -       -       -       -       smtpd
  -o syslog_name=postfix/submission
  -o smtpd_tls_security_level=encrypt
  -o smtpd_sasl_auth_enable=yes
  -o smtpd_client_restrictions=permit_sasl_authenticated,reject
  -o milter_macro_daemon_name=ORIGINATING
smtps     inet  n       -       -       -       -       smtpd
  -o syslog_name=postfix/smtps
  -o smtpd_tls_wrappermode=yes
  -o smtpd_sasl_auth_enable=yes
  -o smtpd_client_restrictions=permit_sasl_authenticated,reject
  -o milter_macro_daemon_name=ORIGINATING
EOF

cat > virtual_alias_maps.cf << EOF
user = mail
hosts = localhost
dbname = mail
query = SELECT destination FROM virtual_aliases WHERE source='%s'
EOF

cat > virtual_mailbox_domains.cf << EOF
user = mail
hosts = localhost
dbname = mail
query = SELECT 1 FROM virtual_domains WHERE name='%s'
EOF

cat > virtual_mailbox_maps.cf << EOF
user = mail
hosts = localhost
dbname = mail
query = SELECT 1 FROM virtual_users WHERE email='%s'
EOF

for s in virtual_alias_maps virtual_mailbox_domains virtual_mailbox_maps 
do
	echo "password = $mail_mysql_passwd" >> $s.cf
done

postmap /etc/postfix/transport


#------------- dovecot ---------------------
echo "Setup dovecot"

cd /etc/dovecot
cat > dovecot.conf <<EOF
protocols = imap lmtp

disable_plaintext_auth = yes
auth_mechanisms = plain login

mail_home = /home/vmail/%d/%n
mail_location = maildir:~

ssl_cert = </etc/ssl/private/vmail.crt
ssl_key = </etc/ssl/private/vmail.key

passdb {
    driver = sql
    args = /etc/dovecot/dovecot-sql.conf
}
userdb {
    driver = static
    args = uid=vmail gid=vmail home=/var/mail/vhosts/%d/%n
}
 
service imap-login {
  inet_listener imap {
    #port = 0
  }
  inet_listener imaps {
    port = 993
    ssl = yes
  }
}

service lmtp {
  unix_listener /var/spool/postfix/private/dovecot-lmtp {
    mode = 0600
    user = postfix
    group = postfix
  }
}

service auth {
    unix_listener /var/spool/postfix/private/auth {
        mode = 0660
        group = postfix
        user = postfix
    }

    unix_listener auth-userdb {
      mode = 0600
      user = vmail
      #group =
    }

    user = dovecot
}

service auth-worker {
  user = vmail
}

EOF

cat > dovecot-sql.conf <<EOF
driver = mysql
default_pass_scheme = SHA512-CRYPT
user_query = SELECT '/home/vmail/%d/%n' as home, 'maildir:/home/vmail/%d/%n' as mail, 5000 AS uid, 5000 AS gid, concat('dirsize:storage=',  quota) AS quota FROM users WHERE email = '%u'
password_query = SELECT email as user, password FROM virtual_users WHERE email='%u';
#password_query = SELECT email as user, password FROM virtual_users WHERE email=(SELECT destination FROM virtual_aliases WHERE source = '%u');
EOF
echo "connect = host=localhost dbname=mail user=mail password=$mail_mysql_passwd" >> dovecot-sql.conf

chown -R vmail:dovecot /etc/dovecot

#--------------test------------------
echo "Testing"
postmap -q test.com mysql:/etc/postfix/virtual_mailbox_domains.cf
postmap -q user1@test.com mysql:/etc/postfix/virtual_mailbox_maps.cf
postmap -q alias1@test.com mysql:/etc/postfix/virtual_alias_maps.cf

dovecot --build-options

#--------------help------------------
echo "Install completed!!!"

echo "If you get similar errors,  use journalctl -xn --unit postfix.service to find out more. "
echo "Add email domain: INSERT INTO \`virtual_domains\`(\`id\`, \`name\`) VALUES('1234', 'example.com')"
echo "Add email user: INSERT INTO \`virtual_users\`(\`domain_id\`, \`password\` , \`email\`) VALUES('1234', ENCRYPT('password', CONCAT('\$6$', SUBSTRING(SHA(RAND()), -16))), 'user1@example.com')"
echo "Add email alias: INSERT INTO \`virtual_aliases\`(\`domain_id\`, \`source\`, \`destination\`) VALUES('1234', 'alias1@example.com', 'user1@example.com')"
echo "Remember to remove test users from database"






