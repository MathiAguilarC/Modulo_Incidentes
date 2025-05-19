const crypto = require('crypto');

const algorithm = 'aes-256-cbc';
const key = crypto.scryptSync('mi_clave_secreta_muy_segura', 'salt', 32);
const iv = Buffer.alloc(16, 0); // Initialization vector

function encrypt(text) {
  // Truncar texto a máximo 50 caracteres para que cifrado hex no exceda 100 caracteres
  const truncatedText = text.length > 50 ? text.substring(0, 50) : text;
  const cipher = crypto.createCipheriv(algorithm, key, iv);
  let encrypted = cipher.update(truncatedText, 'utf8', 'hex');
  encrypted += cipher.final('hex');
  return encrypted;
}

function decrypt(encrypted) {
  const decipher = crypto.createDecipheriv(algorithm, key, iv);
  let decrypted = decipher.update(encrypted, 'hex', 'utf8');
  decrypted += decipher.final('utf8');
  return decrypted;
}

module.exports = { encrypt, decrypt };
