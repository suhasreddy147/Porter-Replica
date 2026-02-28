export const validateEmail = (email: string) =>
  /\S+@\S+\.\S+/.test(email);

export const validatePassword = (password: string) =>
  password.length >= 6;

export const validatePhone = (phone: string) =>
  phone.length >= 7 && phone.length <= 15;