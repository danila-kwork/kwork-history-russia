import os

import requests
import json
from bs4 import BeautifulSoup

filePath = 'questions.json'

urls = []

base_url = "https://kupidonia.ru/list/история%20России"
response = requests.get(base_url)
soup = BeautifulSoup(response.text, 'html.parser')

for item in soup.find_all("a", class_="item"):
    urls.append(f"https://kupidonia.ru/{item['href'].replace('viktoriny','viktoriny-answers')}")

questions = []

for url in urls:
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'html.parser')

    block_question = soup.findAll("div", class_="block_question")

    for block in block_question:
        question = block.find_next("h3", class_="question").find_next("div").text
        answer = block.find_next(class_="answer-text").text
        questions.append({"question": question, "answer": answer})

if os.path.exists(filePath):
    os.remove(filePath)

with open(filePath, 'x') as fp:
    json.dump(questions, fp)
