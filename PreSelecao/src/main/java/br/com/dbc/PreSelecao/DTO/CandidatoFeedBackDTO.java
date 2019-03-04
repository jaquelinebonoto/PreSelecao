package br.com.dbc.PreSelecao.DTO;

import br.com.dbc.PreSelecao.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author jonas.cruz
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoFeedBackDTO {
    private Long id;
    private Status status;
}
