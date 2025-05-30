import React from "react";
import { FieldError, UseFormRegister } from "react-hook-form";

type InputFieldProps = {
  label: string;
  type?: string;
  register: UseFormRegister<any>; // pakai tipe dari react-hook-form
  name: string;
  defaultValue?: string;
  error?: FieldError;
  hidden?: boolean;
  inputProps?: React.InputHTMLAttributes<HTMLInputElement>;
};

const InputField: React.FC<InputFieldProps> = ({
  label,
  type = "text",
  register,
  name,
  defaultValue,
  error,
  hidden,
  inputProps,
}) => {
  return (
    <div className={hidden ? "hidden" : "flex flex-col gap-2 w-full md:w-1/4"}>
      <label className="text-xs text-gray-500" htmlFor={name}>
        {label}
      </label>
      <input
        id={name}
        type={type}
        {...register(name, { value: defaultValue })}
        {...inputProps}
        className={`ring-[1.5px] ring-gray-300 p-2 rounded-md text-sm w-full ${
          error ? "ring-red-400" : ""
        }`}
      />
      {error?.message && (
        <p className="text-xs text-red-400">{error.message.toString()}</p>
      )}
    </div>
  );
};

export default InputField;
