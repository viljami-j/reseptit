@echo off
json-server --watch db.json
start "" http://localhost:3000/recipes/1
