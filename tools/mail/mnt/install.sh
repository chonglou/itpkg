#!/bin/sh

groupadd -g 5000 vmail
useradd -u 5000 -g vmail -s /usr/bin/nologin -d /home/vmail -m vmailo

#------------- mysql ---------------------
root_mysql_passwd="123456"
mail_mysql_passwd="123456"

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
 
MYSQL_PWD=$root_mysql_passwd mysql -u root -p -e "CREATE DATABASE mail; CREATE USER 'mail'@'localhost' IDENTIFIED BY '$mail_mysql_passwd'; GRANT ALL ON mail.* TO 'mail'@'localhost';"

#------------- openssl ---------------------
echo "Generate ssl certs"
cd /etc/ssl/private/
openssl req -new -x509 -nodes -newkey rsa:4096 -keyout vmail.key -out vmail.crt -days 3560 -subj '/CN=itpkg.com/O=IT-PACKAGE LTD./C=US'
chmod 400 vmail.key
chmod 444 vmail.crt

#------------- postfix ---------------------
cd /etc/postfix
cat >> main.cf <<EOF
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
smtpd_sasl_path = /var/run/dovecot/auth-client
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
EOF

cat > virtual_alias_maps.cf <<EOF
user = mail
hosts = localhost
dbname = mail
table = alias
select_field = goto
where_field = address
EOF

cat > virtual_mailbox_domains.cf << EOF
user = mail
hosts = localhost
dbname = mail
table = domain
select_field = domain
where_field = domain
EOF

cat > virtual_mailbox_maps.cf << EOF
user = mail
hosts = localhost
dbname = mail
table = mailbox
select_field = maildir
where_field = username
EOF

cat > virtual_alias_maps.cf << EOF
user = mail
hosts = localhost
dbname = mail
table = domains
select_field = virtual
where_field = domain
EOF

cat > virtual_mailbox_domains.cf << EOF
user = mail
hosts = localhost
dbname = mail
table = forwardings
select_field = destination
where_field = source
EOF

cat > virtual_mailbox_maps.cf << EOF
user = mail
hosts = localhost
dbname = mail
table = users
select_field = concat(domain,'/',email,'/')
where_field = email
EOF

for s in virtual_alias_maps virtual_mailbox_domains virtual_mailbox_maps virtual_alias_maps virtual_mailbox_domains virtual_mailbox_maps
do
	echo "password = $mail_mysql_passwd" >> $s.cf
done

postmap /etc/postfix/transport


#------------- dovecot ---------------------
cd /etc/dovecot
cat > dovecot.conf <<EOF
protocols = imap
auth_mechanisms = plain
passdb {
    driver = sql
    args = /etc/dovecot/dovecot-sql.conf
}
userdb {
    driver = sql
    args = /etc/dovecot/dovecot-sql.conf
}
 
service auth {
    unix_listener auth-client {
        group = postfix
        mode = 0660
        user = postfix
    }
    user = root
}

mail_home = /home/vmail/%d/%n
mail_location = maildir:~

ssl_cert = </etc/ssl/private/vmail.crt
ssl_key = </etc/ssl/private/vmail.key
EOF

cat > dovecot-sql.conf <<EOF
driver = mysql
default_pass_scheme = SHA512-CRYPT
user_query = SELECT '/home/vmail/%d/%n' as home, 'maildir:/home/vmail/%d/%n' as mail, 5000 AS uid, 5000 AS gid, concat('dirsize:storage=',  quota) AS quota FROM users WHERE email = '%u'
password_query = SELECT email as user, password, '/home/vmail/%d/%n' as userdb_home, 'maildir:/home/vmail/%d/%n' as userdb_mail, 5000 as  userdb_uid, 5000 as userdb_gid FROM users WHERE email = '%u'
EOF
echo "connect = host=localhost dbname=mail user=mail password=$mail_mysql_passwd" >> dovecot-sql.conf

echo "Install completed!!!"



