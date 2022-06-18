import ReactPagination from "react-paginate";
import { useEffect } from "react";
import styles from "./style.module.scss";

interface PaginationProps {
    pagination: Pagination;
    handleFunction: (page: Pagination) => void;
}

const Pagination = ({ pagination, handleFunction }: PaginationProps) => {
    useEffect(() => {}, [pagination.totalPages]);

    const handlePageClick = (e: any) => {
        const currentPage = e.selected + 1;
        handleFunction({ ...pagination, currentPage: currentPage });
    };

    return (
        <ReactPagination
            className={styles.pagination_wrapper}
            pageCount={pagination.totalPages as number}
            pageRangeDisplayed={5}
            onPageChange={handlePageClick}
            marginPagesDisplayed={0}
            breakLabel={""}
            previousLabel="<"
            nextLabel=">"
            pageLinkClassName={styles.pagination_link}
            breakClassName={styles.pagination_link_label}
            nextClassName={styles.pagination_link_label}
            previousClassName={styles.pagination_link_label}
            activeLinkClassName={styles.current}
        />
    );
};

export default Pagination;
