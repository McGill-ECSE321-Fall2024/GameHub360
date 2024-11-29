import React, { useRef } from "react";

interface CodeInputFormProps {
  length: number; // Number of input fields
  onComplete: (code: string) => void; // Callback when all inputs are filled
}

const CodeInputForm: React.FC<CodeInputFormProps> = ({ length, onComplete }) => {
  const inputsRef = useRef<HTMLInputElement[]>([]);

  const handleKeyUp = (
    e: React.KeyboardEvent<HTMLInputElement>,
    index: number
  ) => {
    const target = e.target as HTMLInputElement;

    if (e.key === "Backspace" && target.value === "" && index > 0) {
      inputsRef.current[index - 1]?.focus();
    } else if (target.value.length === 1 && index < length - 1) {
      inputsRef.current[index + 1]?.focus();
    }

    if (inputsRef.current.every((input) => input?.value.length === 1)) {
      const code = inputsRef.current.map((input) => input?.value).join("");
      onComplete(code);
    }
  };

  const handlePaste = (e: React.ClipboardEvent<HTMLInputElement>) => {
    e.preventDefault();
    const pasteData = e.clipboardData.getData("text").replace(/\D/g, ""); // Remove non-numeric characters
    pasteData.split("").forEach((char, index) => {
      if (inputsRef.current[index]) {
        inputsRef.current[index].value = char;
      }
    });
    if (pasteData.length === length) {
      onComplete(pasteData);
    }
  };

  const handleInput = (e: React.FormEvent<HTMLInputElement>) => {
    const target = e.target as HTMLInputElement;
    target.value = target.value.replace(/\D/g, ""); // Remove non-numeric characters
  };

  return (
    <form className="max-w-sm">
      <p
        id="helper-text-explanation"
        className="mb-2 text-sm text-gray-500 dark:text-gray-400"
      >
        16-digit card number
      </p>
      <div className="flex space-x-2 rtl:space-x-reverse">
        {Array.from({ length }).map((_, index) => (
          <div key={index}>
            <label htmlFor={`code-${index + 1}`} className="sr-only">
              Digit {index + 1}
            </label>
            <input
              ref={(el) => el && (inputsRef.current[index] = el)}
              type="text"
              id={`code-${index + 1}`}
              maxLength={1}
              className="block w-9 h-9 py-3 text-sm font-extrabold text-center text-gray-900 bg-white border border-gray-300 rounded-lg focus:ring-primary-500 focus:border-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
              onKeyUp={(e) => handleKeyUp(e, index)}
              onPaste={handlePaste}
              onInput={handleInput} // Restrict non-numeric input
              required
            />
          </div>
        ))}
      </div>
    </form>
  );
};

export default CodeInputForm;
