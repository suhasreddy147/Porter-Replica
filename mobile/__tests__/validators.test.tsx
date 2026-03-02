import {
    validateEmail,
    validatePassword,
    validatePhone,
} from "../src/utils/validators";

describe("Validators", () => {
  it("validates email correctly", () => {
    expect(validateEmail("test@test.com")).toBe(true);
    expect(validateEmail("invalid")).toBe(false);
  });

  it("validates password correctly", () => {
    expect(validatePassword("123456")).toBe(true);
    expect(validatePassword("123")).toBe(false);
  });

  it("validates phone correctly", () => {
    expect(validatePhone("6362959439")).toBe(true);
    expect(validatePhone("123")).toBe(false);
  });
});