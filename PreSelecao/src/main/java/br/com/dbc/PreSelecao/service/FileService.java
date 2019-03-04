
package br.com.dbc.PreSelecao.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

@Service
public class FileService {
    
    private final String backDiretorio = "src/main/webapp";
    private final String diretorioDeArquivos = "/arquivos/";
    
    public String uploadFile(String fileBase64, String nomeCandidato) throws IOException {
        Base64 base64Bin = new Base64();
        
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/HH-mm-ss-"));
        String candidato = StringUtils.stripAccents(StringUtils.deleteWhitespace(nomeCandidato));
        
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(backDiretorio);
        stringBuilder.append(diretorioDeArquivos);
        stringBuilder.append(date);
        stringBuilder.append(candidato);
        stringBuilder.append(".pdf");
        
        String nomeCompletoArquivo = stringBuilder.toString();
                
        FileUtils.writeByteArrayToFile(
                                new File(nomeCompletoArquivo),
                                base64Bin.decode(fileBase64.replaceAll("^.{0,30},", ""))
                            ,false);
        
        return diretorioDeArquivos + date + candidato + ".pdf";
    }
}
