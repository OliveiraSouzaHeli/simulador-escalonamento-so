import java.io.*;
import java.util.*;

public class LeitorArquivo {

    public static Entrada ler(String caminho) {
        List<Processo> processos = new ArrayList<>();
        int quantum = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {

            String linha;
            int numeroLinha = 0;

            while ((linha = br.readLine()) != null) {
                numeroLinha++;

                linha = linha.trim();

                // Ignora linhas vazias e comentários
                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }

                // Leitura do QUANTUM
                if (linha.startsWith("QUANTUM=")) {
                    try {
                        quantum = Integer.parseInt(linha.split("=")[1].trim());
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ Erro ao ler QUANTUM na linha " + numeroLinha);
                    }
                    continue;
                }

                // 🔥 CORREÇÃO AQUI
                try {
                    String[] partes = linha.split("\\s+");

                    if (partes.length < 3) {
                        System.out.println("⚠️ Formato inválido na linha " + numeroLinha + ": " + linha);
                        continue;
                    }

                    String nome = partes[0];
                    int chegada = Integer.parseInt(partes[1]);
                    int burst = Integer.parseInt(partes[2]);
                    int prioridade = (partes.length > 3)
                            ? Integer.parseInt(partes[3])
                            : 0;

                    processos.add(new Processo(nome, chegada, burst, prioridade, quantum));

                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Número inválido na linha " + numeroLinha + ": " + linha);
                }
            }

        } catch (IOException e) {
            System.out.println("❌ Erro ao abrir o arquivo: " + caminho);
            e.printStackTrace();
        }

        return new Entrada(processos, quantum);
    }
}