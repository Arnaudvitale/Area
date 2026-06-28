# Ruby On Rails stack documentation
The Ruby On Rails stack utilize Ruby, Rails and Active Record.

Below are the advantages and disadvantages of this stack.

## Advantages
- Ruby has a simple syntaxe
- The database is included with Rails
- Rails is a powerful tools for MVC websites

## Disavantages

- It can't be used for a mobile app which mean a second technology has to used

## How to use

Before starting, ensure the following are installed on your computer:
- **ruby**
- **rails**

Here is the commands lines to install Ruby:

```
sudo dnf install ruby
```
```
sudo dnf group install "C Development Tools and Libraries"
```
```
sudo dnf install ruby-devel zlib-devel
```
```
gem install rails
```

Before starting the project, navigate to the *area/* directory and execute these commands before first use:

```
bundle install
```
```
bin/rails db:migrate
```

To start the project, stay in the folder and execute this command:

```
bin/rails server
```

Server is running on port: 3000 <br/>
routes available: [Web view](http://localhost:3000/flux)