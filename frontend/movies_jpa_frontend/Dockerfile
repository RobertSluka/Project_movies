# Use official Node.js image
FROM node:18-alpine

# Set working directory
WORKDIR /usr/app

# Copy only package.json and package-lock.json first for better caching
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the application (for production)
RUN npm run build

# Expose development port
EXPOSE 3000

# Start the application
CMD ["npm", "start"]
