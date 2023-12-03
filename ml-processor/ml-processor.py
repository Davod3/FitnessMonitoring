from flask import Flask, request, jsonify
import sklearn
from joblib import load

app = Flask(__name__)

loaded_model = load('model/trained_model.joblib')

def perform_prediction(instance):
    prediction = loaded_model.predict([instance])[0]
    return prediction

@app.route('/predict', methods=['POST'])
def predict_activity():
    #Receive data from processor
    instance = request.get_json()

    required_attributes = ['acceleration_x', 'acceleration_y', 'acceleration_z', 'gyro_x', 'gyro_y', 'gyro_z']
    if not all(attribute in instance for attribute in required_attributes):
        return jsonify({"error": "Invalid JSON format. Missing required attributes."}), 400

    try:
        instance_for_prediction = [float(instance[attribute]) for attribute in required_attributes]
    except ValueError:
        return jsonify({"error": "Invalid atribute values. Unable to convert to float."}), 400

    prediction = perform_prediction(instance_for_prediction)

    instance_predicted = {
        'date': instance.get('date'),
        'time': instance.get('time'),
        'activity': int(prediction),
        'acceleration_x': instance.get('acceleration_x'),
        'acceleration_y': instance.get('acceleration_y'),
        'acceleration_z': instance.get('acceleration_z'),
        'gyro_x': instance.get('gyro_x'),
        'gyro_y': instance.get('gyro_y'),
        'gyro_z': instance.get('gyro_z'),
    }

    return jsonify(instance_predicted)

@app.route('/', methods=['GET'])
def run():
    return "{\"message\":\"Hello world\"}"


if __name__ == '__main__':
    app.run(host="0.0.0.0", port=int("3000"), debug=True)