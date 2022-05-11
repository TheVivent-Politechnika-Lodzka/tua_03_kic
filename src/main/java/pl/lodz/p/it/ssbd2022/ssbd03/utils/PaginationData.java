package pl.lodz.p.it.ssbd2022.ssbd03.utils;

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
    private int totalCount;
    private List data;
}
