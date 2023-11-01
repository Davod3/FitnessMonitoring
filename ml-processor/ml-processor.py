from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/', methods=['GET'])
def run():
    return "{\"message\":\"Hello world\"}"

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=int("3000"), debug=True)