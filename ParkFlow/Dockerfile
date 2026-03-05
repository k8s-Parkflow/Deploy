# 1단계 : 빌드 스테이지
FROM node:18-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

# Vite - 도메인 사용
ENV VITE_API_URL=http://parking-backend.local


RUN npm run build

# 2단계 : 실행 스테이지
FROM nginx:stable-alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf

# Vite는 dist 폴더
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
