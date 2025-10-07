package br.com.tick.tickdesck.models.files.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// @Data: Gera getters, setters, toString, equals e hashCode.
@Data
// @AllArgsConstructor: Gera um construtor com todos os campos como parâmetros.
@AllArgsConstructor
// @NoArgsConstructor: Gera um construtor sem parâmetros.
@NoArgsConstructor
public class FileDto {
    // Identificador do arquivo
    Long id;
    // Nome do arquivo
    String name;
    // Tipo do arquivo (ex: extensão)
    String type;
    // Caminho do arquivo
    String path;
}
