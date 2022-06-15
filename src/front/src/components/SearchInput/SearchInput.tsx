import { faSearch } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Input } from "@mantine/core";
import styles from "./style.module.scss";

interface SearchInputProps {
    placeholder: string;
    onChange?: (value: string) => void;
}

const SearchInput = ({placeholder, onChange} : SearchInputProps) => {
    return (
        <div className={styles.search_input_wrapper}>
            <input
                placeholder={placeholder}
                type="text"
                className={styles.search_input}
            />
            <FontAwesomeIcon icon={faSearch} className={styles.icon} />
        </div>
    );
};

export default SearchInput;
