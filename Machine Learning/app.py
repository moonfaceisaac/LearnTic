from flask import Flask, request, jsonify


app = Flask(__name__)

# Load model and preprocessors
model = load_model("best_model.h5")
preprocessor = joblib.load("preprocessor.pkl")
label_encoder = joblib.load("label_encoder.pkl")

@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json()

    try:
        # Extract input features
        tugas = int(data['Persentase Tugas'])
        absen = int(data['Jumlah Ketidakhadiran'])
        rata = int(data['Rata-rata'])

        # Convert to DataFrame
        df = pd.DataFrame([{
            'Persentase Tugas': tugas,
            'Jumlah Ketidakhadiran': absen,
            'Rata-rata': rata
        }])

        # Preprocess
        X_input = preprocessor.transform(df)

        # Predict
        y_pred_proba = model.predict(X_input)
        predicted_index = np.argmax(y_pred_proba, axis=1)[0]
        predicted_label = label_encoder.inverse_transform([predicted_index])[0]

        return jsonify({
            'predicted_label': predicted_label,
            'probabilities': y_pred_proba[0].tolist()
        })

    except Exception as e:
        return jsonify({'error': str(e)})

if __name__ == '__main__':
    app.run(debug=True)