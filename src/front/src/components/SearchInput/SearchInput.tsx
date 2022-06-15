import { faSearch } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState } from "react";
import styles from "./style.module.scss";

interface SearchInputProps {
    placeholder: string;
    onChange: (value: string) => void;
}

const SearchInput = ({ placeholder, onChange }: SearchInputProps) => {
    const [value, setValue] = useState<string>("");
    return (
        <div className={styles.search_input_wrapper}>
            <input
                value={value}
                placeholder={placeholder}
                type="text"
                className={styles.search_input}
                onChange={(e) => {
                    setValue(e.target.value);
                    onChange(e.target.value);
                }}
            />
            <FontAwesomeIcon icon={faSearch} className={styles.icon} />
        </div>
    );
};

export default SearchInput;
