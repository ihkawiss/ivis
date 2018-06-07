#!/bin/bash
export PATH=$PATH:/opt/gradle/gradle-4.7/bin

# pull latest code from git
echo "Pulling latest code from github!"
cd ..
git pull

# build frontend
echo "Building frontend application!"
cd frontend
rm -rf /var/www/html/*
cp -r ./* /var/www/html/

echo "deployment of client application done!"

# build backend
echo "Building backend application!"
cd ../api
rm -rf build
gradle build -x test

systemctl stop ivispro
rm /var/ivispro/ivispro-*.jar

cp build/libs/ivispro-*.jar /var/ivispro/
systemctl start ivispro

echo "deployment of backend application finished"
