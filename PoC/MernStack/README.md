# Mern stack documentation
Mern stack is using node, express, react and mongoDb

You can find below what's the advantages and inconvenients

## Advantages

- Client and Server side. Backend and Frontend
<br/>
- Node, express and react are popular programmation langages with a large community and lots of documentation
<br/>
- They are optimised for web and servers and can be adapted to application developpement with React Native
<br/>
- MongoDB is a simple technology for visualize and manage databases with a web/ui version. (it doesnt use sql)
<br/>
- Simple to dockerize
</br>
- Efficient Data handling

## Disadvantages

- Project is heavier with all packages and node

- Lots of technologies to learn

- High depedency to JavaScript

## How to use

Before start, check that you have correctly install node on your computer

You can download it here: [Node Download](https://nodejs.org/en/)

When it's done, open a terminal and go to *server/* and execute the following command:

``` node
node server.js
```

*The server should start and display you this message:*
```
Server is running on port: 8000
Successfully connected to MongoDB Atlas!
Collections de la base:
        users
```

After you can go to *reactweb/* directory and execute this command:

``` npm
npm start
```

The web page open (port: 3000 or 3030) and it is done ! You can now use the website for create, read, update and delete records of the database

routes available:

- **/record** (List of records/users)

- **/record:id** (Show record details)

- **/record/add** (add new record)

- **/record/update:id** (update a record)

- **/:id** (with delete method, it destroy)