/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

const {onRequest} = require("firebase-functions/v2/https");
const logger = require("firebase-functions/logger");

// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started

// exports.helloWorld = onRequest((request, response) => {
//   logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
const functions = require('firebase-functions');
const nodemailer = require('nodemailer');

// Cấu hình transport cho email
const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'n20dccn100@student.ptithcm.edu.vn',
        pass: 'hien352636880'
    }
});

// Hàm gửi email
exports.sendEmail = functions.https.onCall((data, context) => {
    const mailOptions = {
        from: 'n20dccn100@student.ptithcm.edu.vn',
        to: data.email,
        subject: data.subject,
        text: data.message
    };

    return transporter.sendMail(mailOptions, (error, info) => {
        if (error) {
            return { success: false, error: error.toString() };
        }
        return { success: true };
    });
});