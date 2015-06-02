var gulp = require('gulp');
var coffee = require('gulp-coffee');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var imagemin = require('gulp-imagemin');
var sourcemaps = require('gulp-sourcemaps');
var htmlmin = require('gulp-htmlmin');
var minifyCss = require('gulp-minify-css');
var connect = require('gulp-connect');
var revappend = require('gulp-version-append');
var del = require('del');
var pkginfo = require('./package.json')

var paths = {
  public: 'public',
  scripts: 'assets/javascripts/**/*.js',
  styles: 'assets/stylesheets/**/*.css',
  templates: 'assets/templates/**/*.html',
  images: 'assets/images/**/*',
  third: [
    'node_modules/jquery/dist/jquery.min.js',

    'node_modules/bootstrap/dist/css/bootstrap.min.css',
    'node_modules/bootstrap/dist/js/bootstrap.min.js',
    'node_modules/bootstrap/dist/fonts/*',

    'node_modules/angular/angular.min.js',
    'node_modules/angular-route/angular-route.min.js',
    'node_modules/angular-animate/angular-animate.min.js',
    'node_modules/angular-sanitize/angular-sanitize.min.js',
    'node_modules/angular-resource/angular-resource.min.js',
    'node_modules/angular-cookies/angular-cookies.min.js',
    'node_modules/angular-translate/dist/angular-translate.min.js',
    'node_modules/angular-translate-storage-cookie/angular-translate-storage-cookie.min.js',
    'node_modules/angular-bootstrap/dist/ui-bootstrap-tpls.min.js',

    'node_modules/famfamfam-flags/dist/**/*',
    'node_modules/famfamfam-silk/dist/**/*',
    'node_modules/moment/min/moment-with-locales.min.js'
  ]
};

gulp.task('clean', function(cb) {
  del([paths.public], cb);
});

gulp.task('styles', function() {
  return gulp.src(paths.styles)
    .pipe(sourcemaps.init())
    //.pipe(minifyCss({compatibility: 'ie8'}))
    .pipe(minifyCss())
    .pipe(concat('all.min.css'))
    .pipe(sourcemaps.write())
    .pipe(gulp.dest(paths.public));
});

gulp.task('3rd', function() {
   return gulp.src(paths.third, {"base":"."})
     .pipe(gulp.dest(paths.public));
});

gulp.task('scripts', function() {
  return gulp.src(paths.scripts)
    .pipe(sourcemaps.init())
    //.pipe(coffee())
    .pipe(revappend(['html', 'js', 'css']))
    .pipe(uglify())
    .pipe(concat('all.min.js'))
    .pipe(sourcemaps.write())
    .pipe(gulp.dest(paths.public));
});

gulp.task('images', function() {
  return gulp.src(paths.images)
    .pipe(imagemin({optimizationLevel: 5}))
    .pipe(gulp.dest(paths.public+'/images'));
});

gulp.task('templates', function() {
  return gulp.src(paths.templates)
    .pipe(revappend(['html', 'js', 'css']))
    .pipe(htmlmin({collapseWhitespace: true}))
    .pipe(gulp.dest(paths.public))
});

gulp.task('mailer', function() {
  return gulp.src('assets/mailer/**/*.html')
    .pipe(revappend(['html', 'js', 'css']))
    .pipe(htmlmin({collapseWhitespace: true}))
    .pipe(gulp.dest('mailer'))
});

gulp.task('watch', function() {
  gulp.watch(paths.scripts, ['scripts']);
  gulp.watch(paths.styles, ['styles']);
  gulp.watch(paths.templates, ['templates']);
  gulp.watch(paths.images, ['images']);
});

gulp.task('server', function() {
  connect.server({
       root: paths.public,
       //livereload: true,
       //fallback: 'index.html',
       port: 8000
   });
});

gulp.task('default', ['watch', 'build']);

gulp.task('build', ['templates', 'mailer', 'scripts', '3rd', 'styles', 'images']);
