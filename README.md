<div align="center" id="top"> 
  <img src="./.github/app.gif" alt="Spring mail" />

&#xa0;

  <!-- <a href="https://gestionstock.netlify.app">Demo</a> -->
</div>

<h1 align="center">Account management </h1>

<p align="center">
  <img alt="Github top language" src="https://img.shields.io/github/languages/top/Abdelouahedd/account-management?color=56BEB8">

  <img alt="Github language count" src="https://img.shields.io/github/languages/count/Abdelouahedd/account-management?color=56BEB8">

  <img alt="Repository size" src="https://img.shields.io/github/repo-size/Abdelouahedd/account-management?color=56BEB8">

  <img alt="License" src="https://img.shields.io/github/license/Abdelouahedd/account-management?color=56BEB8">

  <img alt="Github issues" src="https://img.shields.io/github/issues/Abdelouahedd/account-management?color=56BEB8" />

  <img alt="Github forks" src="https://img.shields.io/github/forks/Abdelouahedd/account-management?color=56BEB8" /> 

  <img alt="Github stars" src="https://img.shields.io/github/stars/Abdelouahedd/account-management?color=56BEB8" /> 
</p>

Status

 <h4 align="center"> 
	🚧  Spring mail  🚀 Under construction...  🚧
</h4> 

<hr> 

<p align="center">
  <a href="#dart-about">About</a> &#xa0; | &#xa0; 
  <a href="#sparkles-features">Features</a> &#xa0; | &#xa0;
  <a href="#rocket-technologies">Technologies</a> &#xa0; | &#xa0;
  <a href="#white_check_mark-requirements">Requirements</a> &#xa0; | &#xa0;
  <a href="#checkered_flag-starting">Starting</a> &#xa0; | &#xa0;
  <a href="#memo-license">License</a> &#xa0; | &#xa0;
  <a href="https://github.com/abdelouahedd" target="_blank">Author</a>
</p>

<br>

## :dart: About ## 

Project is about manage users of application, you can create users and verify there emails, and if a user want to rest password it contains a end point to send email verification
<!-- Describe your project -->

## :sparkles: Features ##

:heavy_check_mark: Feature 1 : config the server with the application properties\
:heavy_check_mark: Feature 2 : send emails from the server\
:heavy_check_mark: Feature 3 : add integration test to the project

## :rocket: Technologies ##

The following tools were used in this project:

- [Spring](https://spring.io/)
- [Maven](http://maven.apache.org/)
- [Java 11](https://www.java.com/)
- [Docker](https://www.docker.com/)

## :white_check_mark: Requirements ##

Before starting :checkered_flag:, you need to have [Git](https://git-scm.com) and [Docker](https://www.docker.com/)
installed.

## :checkered_flag: Starting ##

```bash
#Before use the repo run container of mail host for testing
$ docker network create mail-network
$ docker run --name=mailsmtp --network=mail-network -p 1025:1025 -p 8025:8025 mailhog/mailhog:v1.0.1

# Clone this project
$ git clone https://github.com/Abdelouahedd/account-management

# Access
$ cd account-management

# build image
$ docker build -t account-management .

#run container
$ docker run --name=mailtest -e SPRING.MAIL.HOST="mailsmtp" --network=mail-network -d -p 8080:8080 account-management  

# The server will initialize in the <http://localhost:8080>
```

## :memo: License ##

This project is under license from MIT. For more details, see the [LICENSE](LICENSE) file.

Made with :heart: by <a href="https://github.com/abdelouahedd" target="_blank">Abdelouahed</a>

&#xa0;

<a href="#top">Back to top</a>
