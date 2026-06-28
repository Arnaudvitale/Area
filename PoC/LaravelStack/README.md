# Laravel stack documentation
The Laravel stack utilizes PHP, MySQL/MariaDB, and Apache.

Below are the advantages and disadvantages of this stack.

## Advantages

- PHP is a widely used programming language with a substantial community.
- PHP is a stable language optimized for web and server environments.
- MariaDB is an accessible technology for visualizing and managing databases.

## Disadvantages

- PHP, being an older language, might face risks of reduced maintenance in the future.
- PHP is not the most performant language in some scenarios.
- It is not ideally suited for application development in certain contexts.
- There is a risk of obsolescence, as newer languages and technologies may offer more advanced features.

## How to use

Before starting, ensure the following are installed on your computer:
- **php**
- **mariadb**
- **httpd** (Apache)

Here is the command line to install PHP and its dependencies:

```
sudo dnf install php php-cli php-fpm php-json php-common php-mbstring php-xml php-zip php-gd php-curl
```

To start the project, navigate to the *testWeb/* directory and execute this command:

```
php artisan serve
```

Server is running on port: 8000 <br/>
routes available: [Web view](http://localhost:8000/users)