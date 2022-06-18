import ReactModal from "react-modal";

interface ModalProps {
    isOpen: boolean;
    children: JSX.Element;
}

const Modal = ({ isOpen, children }: ModalProps) => {
    const customStyles = {
        overlay: {
            backgroundColor: "rgba(0, 0, 0, 0.85)",
        },
        content: {
            width: "100vw",
            height: "100vh",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            backgroundColor: "transparent",
            border: "none",
        },
    };

    return (
        <ReactModal isOpen={isOpen} style={customStyles} ariaHideApp={false}>
            {children}
        </ReactModal>
    );
};

export default Modal;
