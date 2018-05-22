import net.jini.space.JavaSpace;
import net.jini.core.lease.Lease;

import java.util.*;

public class BatePapo {

    private static String entrarAmbiente(JavaSpace space, Scanner scanner, String nome) throws Exception {
        List<String> listaAmb = new ArrayList<String>();

        do {
            List<Ambiente> listaAmbObj = Helpers.listaAmbiente(space);
            for (int i = 0; i < listaAmbObj.size(); i++) {
                listaAmb.add(listaAmbObj.get(i).nome);
            }
            listaAmbObj.clear();

            listaAmb.remove(nome);

            if (listaAmb.size() == 0) {
                if (nome == null) {
                    continue;
                }
                else {
                    System.out.println("\r\nNao ha outro ambiente disponivel");
                    return null;
                }
            }

            System.out.println("\r\nAmbientes disponiveis:");
            for (int i = 0; i < listaAmb.size(); i++) {
                System.out.println(listaAmb.get(i));
            }

            if (nome == null) {
                System.out.print("\r\nEscolha o ambiente a entrar (ou ENTER para encerrar): ");
            }
            else {
                System.out.print("\r\nEscolha o novo ambiente a entrar (ou ENTER para cancelar): ");
            }

            String newAmb = scanner.nextLine();
            
            if (newAmb == null || newAmb.equals("")) {
                break;
            }
            else if (listaAmb.contains(newAmb)) {
                listaAmb.clear();
                return newAmb;
            }
            else {
                System.out.println("\r\nAmbiente invalido");

                if (nome == null) {
                    listaAmb.clear();
                }
                else {
                    break;
                }
            }
        } while (listaAmb.size() == 0);

        listaAmb.clear();
        return null;
    }

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

            User usuario = new User();
            int userIndex = 0;

            while (true) {
                userIndex += 1;
                String userName = "user" + userIndex;
                usuario.nome = userName;
                User tempUser = (User) space.read(usuario, null, JavaSpace.NO_WAIT);

                if (tempUser == null) {
                    space.write(usuario, null, Lease.FOREVER);
                    System.out.println("\r\nUsuario registrado como: " + usuario.nome);
                    break;
                }
            }

            System.out.println("\r\n\r\nProcurando ambientes . . .");
            String ambAtual = entrarAmbiente(space, scanner, null);
            usuario = (User) space.take(usuario, null, JavaSpace.NO_WAIT);

            if (ambAtual == null) {
                System.exit(0);
            }

            usuario.amb = ambAtual;
            space.write(usuario, null, Lease.FOREVER);
            System.out.println("\r\nEntrou no ambiente: " + ambAtual);
            System.out.println("\r\nPress any key to continue . . .");
            scanner.nextLine();

            boolean pausar;
            while (true) {
                System.out.println("\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
                System.out.println("MENU");
                System.out.println("1 - Enviar mensagem");
                System.out.println("1 - Receber novas mensagens");
                System.out.println("3 - Mudar de ambiente");
                System.out.println("4 - Listar todos os ambientes existentes");
                System.out.println("5 - Listar todos os dispositivos no ambiente atual");
                System.out.println("6 - Lista todos os usuarios no ambiente atual");
                
                System.out.println("\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
                System.out.print("Entre com a opcao desejada (ou ENTER para sair): ");
                String opcao = scanner.nextLine();
                if (opcao == null || opcao.equals("")) {
                    break;
                }
                else {
                    pausar = true;
                    switch (opcao) {

                        case "1":
                        {
                            List<User> listaUser = Helpers.listaUsuario(space, ambAtual);
                            List<String> enderecaveis = new ArrayList<String>();
                            
                            System.out.println("\r\nUsuarios enderecaveis:");
                            for (int i = 0; i < listaUser.size(); i++) {
                                String userName = listaUser.get(i).nome;

                                if (userName.equals(usuario.nome)) {
                                    continue;
                                }

                                enderecaveis.add(userName);
                                System.out.println(userName);
                            }
                            listaUser.clear();

                            if (enderecaveis.size() == 0) {
                                System.out.println("Nao ha usuario enderecavel");
                                break;
                            }

                            System.out.print("\r\nEntre com o nome do usuario alvo (ou ENTER para cancelar): ");
                            String nomeTo = scanner.nextLine();

                            if (nomeTo == null || nomeTo.equals("")) {
                                pausar = false;
                            }
                            else if (enderecaveis.contains(nomeTo)) {
                                Mensagem novaMsg = new Mensagem();
                                novaMsg.time = System.currentTimeMillis();
                                novaMsg.from = usuario.nome;
                                novaMsg.to = nomeTo;

                                System.out.println("\r\nEntre com a mensagem a enviar:");
                                novaMsg.msg = scanner.nextLine();

                                space.write(novaMsg, null, Lease.FOREVER);
                                System.out.println("\r\nMensagem enviada a " + nomeTo);
                            }
                            else {
                                System.out.println("\r\nUsuario invalido");
                            }

                            enderecaveis.clear();
                            break;
                        }

                        case "2":
                        {
                            Mensagem msgTemplate = new Mensagem();
                            msgTemplate.to = usuario.nome;

                            Mensagem novaMsg = (Mensagem) space.take(msgTemplate, null, JavaSpace.NO_WAIT);

                            if (novaMsg == null) {
                                System.out.println("\r\nNao ha novas mensagens");
                            }
                            else {
                                System.out.println();

                                while (novaMsg != null) {
                                    System.out.print("\r\n" + new Date(novaMsg.time));
                                    System.out.println("\t - \tEnviada por: " + novaMsg.from);
                                    System.out.println(novaMsg.msg);
    
                                    novaMsg = (Mensagem) space.take(msgTemplate, null, JavaSpace.NO_WAIT);
                                }

                                System.out.println();
                            }

                            break;
                        }

                        case "3":
                        {
                            String novoAmb = entrarAmbiente(space, scanner, ambAtual);

                            if (novoAmb != null) {
                                usuario.amb = null;
                                usuario = (User) space.take(usuario, null, JavaSpace.NO_WAIT);
                                usuario.amb = novoAmb;
                                space.write(usuario, null, Lease.FOREVER);

                                ambAtual = novoAmb;
                                System.out.println("\r\nMudou para o ambiente: " + ambAtual);
                            }

                            break;
                        }

                        case "4":
                        {
                            List<Ambiente> listaAmb = Helpers.listaAmbiente(space);
                            
                            System.out.println("\r\nAmbientes encontrados:");
                            for (int i = 0; i < listaAmb.size(); i++) {
                                System.out.println(listaAmb.get(i).nome);
                            }
                            listaAmb.clear();

                            break;
                        }

                        case "5":
                        {
                            List<Dispositivo> listaDisp = Helpers.listaDispositivo(space, ambAtual);
                            if (listaDisp.size() == 0) {
                                System.out.println("Nao ha dispositivos");
                                break;
                            }
                                
                            System.out.println("\r\nDispositivos encontrados no ambiente " + ambAtual + ":");
                            for (int i = 0; i < listaDisp.size(); i++) {
                                System.out.println(listaDisp.get(i).nome);
                            }
                            listaDisp.clear();
                            break;
                        }

                        case "6":
                        {
                            List<User> listaUser = Helpers.listaUsuario(space, ambAtual);
                                
                            System.out.println("\r\nUsuarios encontrados no ambiente " + ambAtual + ":");
                            for (int i = 0; i < listaUser.size(); i++) {
                                System.out.println(listaUser.get(i).nome);
                            }
                            
                            listaUser.clear();
                            break;
                        }

                        default:
                            System.out.println("\r\nOpcao invalida");
                            break;
                    }

                    if (pausar) {
                        System.out.println("\r\nPress any key to continue . . .");
                        scanner.nextLine();
                    }
                }
            }

            usuario.amb = null;
            space.take(usuario, null, JavaSpace.NO_WAIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
