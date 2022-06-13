import { createContext } from "react";
import { useReducer } from "react";

// STATE

interface ContextState {
    isFirstNameValid: boolean;
    isLastNameValid: boolean;
    isPhoneNumberValid: boolean;
    isPESELValid: boolean;
    isEmailValid: boolean;
    isPasswordValid: boolean;
    input: string;
}

// ACTION
export type ActionType =
    | "VALIDATE_FIRSTNAME"
    | "VALIDATE_LASTNAME"
    | "VALIDATE_EMAIL"
    | "VALIDATE_PHONENUMBER"
    | "VALIDATE_PESEL"
    | "VALIDATE_PASSWORD"
    | "RESET_VALIDATION";

interface ValidationAction {
    payload: ContextState;
    type: ActionType;
}

// CONTEXT

interface ValidationContext {
    state: ContextState;
    dispatch: (action: ValidationAction) => void;
}

// INITIAL STATE

const initialState: ContextState = {
    isFirstNameValid: true,
    isLastNameValid: true,
    isPhoneNumberValid: true,
    isPESELValid: true,
    isEmailValid: true,
    isPasswordValid: true,
    input: "",
};

const validate = (input: string, pattern: RegExp) => {
    return pattern.test(input);
};

const validationReducer = (state: ContextState, action: ValidationAction) => {
    state.input = "";
    switch (action.type) {
        case "VALIDATE_FIRSTNAME": {
            return {
                ...state,
                isFirstNameValid: validate(
                    action.payload.input,
                    /^[a-zA-Z\u00C0-\u017F'][a-zA-Z-\u00C0-\u017F' ]+[a-zA-Z\u00C0-\u017F']{3,30}$/
                ),
            };
        }
        case "VALIDATE_LASTNAME": {
            return {
                ...state,
                isLastNameValid: validate(
                    action.payload.input,
                    /^[a-zA-Z\u00C0-\u017F'][a-zA-Z-\u00C0-\u017F' ]+[a-zA-Z\u00C0-\u017F']{3,30}$/
                ),
            };
        }
        case "VALIDATE_PHONENUMBER": {
            return {
                ...state,
                isPhoneNumberValid: validate(action.payload.input, /^\d{9}$/),
            };
        }
        case "VALIDATE_PESEL": {
            return {
                ...state,
                isPESELValid: validate(action.payload.input, /^\d{11}$/),
            };
        }
        case "RESET_VALIDATION": {
            return {
                ...initialState,
            };
        }
        default: {
            return { ...state };
        }
    }
};

const validationContext = createContext<ValidationContext>({
    state: initialState,
    dispatch: () => {},
});

const ValidationProvider = ({ children }: { children: JSX.Element }) => {
    const [state, dispatch] = useReducer(validationReducer, initialState);
    const value = { state, dispatch };
    return (
        <validationContext.Provider value={value}>
            {children}
        </validationContext.Provider>
    );
};

export { validationContext, ValidationProvider };