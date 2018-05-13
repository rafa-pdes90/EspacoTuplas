import net.jini.space.JavaSpace;
import net.jini.core.lease.Lease;

import java.util.Queue;
import java.util.Scanner;

public class ControleCentral {

    public static void main(String[] args) {
        try {
            System.out.println("Procurando pelo servico JavaSpace...");
            Lookup finder = new Lookup(JavaSpace.class);
            JavaSpace space = (JavaSpace) finder.getService();
            if (space == null) {
                    System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
                    System.exit(-1);
            } 
            System.out.println("O servico JavaSpace foi encontrado.");
            
            Scanner scanner = new Scanner(System.in);
            int ambCount = 0;
            while (true) {
                System.out.println("\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
                System.out.println("MENU");
                System.out.println("1 - Criar ambiente");
                System.out.println("2 - Destruir ambiente");
                System.out.println("3 - Criar dispositivo");
                System.out.println("4 - Remover dispositivo");
                System.out.println("5 - Listar todos os ambientes");
                System.out.println("6 - Listar todos os dispositivos de um ambiente");
                System.out.println("7 - Lista todos os usuários em um ambiente");
                
                System.out.println("\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
                System.out.print("Entre com a opcao desejada (ou ENTER para sair): ");
                String opcao = scanner.nextLine();
                if (opcao == null || opcao.equals("")) {
                    System.exit(0);
                }
                else {
                    switch (opcao) {
                        case "1":
                            Ambiente novoAmb = new Ambiente();
                            ambCount += 1;
                            novoAmb.nome = "amb" + ambCount;
                            space.write(novoAmb, null, Lease.FOREVER);
                            break;
                        case "2":
                            Ambiente oldAmb = new Ambiente();
                            oldAmb.nome = "amb" + ambCount;
                            ambCount -= 1;
                            space.take(oldAmb, null, JavaSpace.NO_WAIT);
                            break;
                        case "3":
                            break;
                        case "4":
                            break;
                        case "5":
                            Queue<Ambiente> listaAmb = new Queue<Ambiente>();
                            Ambiente amb;
                            Ambiente template = new Ambiente();
                            do {
                                amb = (Ambiente) space.take(template, null, JavaSpace.NO_WAIT);

                                if (amb != null) {
                                    System.out.println("Ambiente encontrado: " + amb.nome);
                                    listaAmb.add(amb);
                                }
                            } while (amb != null);

                            while (!listaAmb.isEmpty()) {
                                amb = listaAmb.poll();
                                space.write(amb, null, Lease.FOREVER);
                            }
                            break;
                        case "6":
                            break;
                        case "7":
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
