# stage 1
FROM node:alpine as builder
WORKDIR /communitterFrontend
ENV PATH /communitterFrontend/node_modules/.bin:$PATH
COPY package.json /communitterFrontend
COPY package-lock.json /communitterFrontend
RUN npm install
COPY . .
EXPOSE 80
CMD ["npm","start"]
