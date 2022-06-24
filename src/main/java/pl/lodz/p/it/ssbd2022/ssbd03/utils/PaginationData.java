package pl.lodz.p.it.ssbd2022.ssbd03.utils;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Klasa reprezentującą obiekt zwracany podczas stronicowania bazy danych
 */
@Getter
@Setter
@AllArgsConstructor
public class PaginationData {

    private static final long serialVersionUID = 1L;

    @NotNull
    private int totalCount;
    @NotNull
    private int totalPages;
    @NotNull
    private int currentPage;
    @NotNull
    private List data;
}
