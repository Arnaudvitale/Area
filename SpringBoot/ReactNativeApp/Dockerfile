# Image de base
FROM node:20-alpine

# Répertoire de travail
WORKDIR /app

# Copier package.json dans le répertoire de travail
COPY ./package*.json ./

# Lancer l'application
CMD ["sh", "-c", "npm install && npx expo install --fix && npm start"]