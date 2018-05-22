import net.jini.space.JavaSpace;
import net.jini.core.lease.Lease;

import java.util.*;

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
            boolean pausar;
            while (true) {
                System.out.println("\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
                System.out.println("MENU");
                System.out.println("1 - Criar ambiente");
                System.out.println("2 - Destruir ambiente");
                System.out.println("3 - Criar dispositivo");
                System.out.println("4 - Destruir dispositivo");
                System.out.println("5 - Mover dispositivo");
                System.out.println("6 - Listar todos os ambientes existentes");
                System.out.println("7 - Listar todos os dispositivos de um ambiente");
                System.out.println("8 - Lista todos os usuarios em um ambiente");
                
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
                            Ambiente novoAmb = new Ambiente();
                            int ambIndex = 0;

                            while (true) {
                                ambIndex += 1;
                                String ambName = "amb" + ambIndex;
                                novoAmb.nome = ambName;
                                Ambiente tempAmb = (Ambiente) space.read(novoAmb, null, JavaSpace.NO_WAIT);

                                if (tempAmb == null) {
                                    space.write(novoAmb, null, Lease.FOREVER);
                                    System.out.println("\r\nAmbiente " + ambName + " criado");
                                    break;
                                }
                            }
                            break;
                        }

                        case "2":
                        {
                            List<Ambiente> listaAmb = Helpers.listaAmbiente(space);
                            if (listaAmb.size() == 0) {
                                System.out.println("Nao ha ambientes");
                                break;
                            }
                            List<String> destruiveis = new ArrayList<String>();

                            Dispositivo dispTemplate = new Dispositivo();
                            User userTemplate = new User();
                            
                            System.out.println("\r\nAmbientes destruiveis:");
                            for (int i = 0; i < listaAmb.size(); i++) {
                                String ambName = listaAmb.get(i).nome;

                                dispTemplate.amb = ambName;
                                Dispositivo tempDisp = (Dispositivo) space.read(dispTemplate, null, JavaSpace.NO_WAIT);
                                if (tempDisp != null) {
                                    continue;
                                }

                                userTemplate.amb = ambName;
                                User tempUser = (User) space.read(userTemplate, null, JavaSpace.NO_WAIT);
                                if (tempUser != null) {
                                    continue;
                                }

                                destruiveis.add(ambName);
                                System.out.println(ambName);
                            }
                            listaAmb.clear();

                            if (destruiveis.size() == 0) {
                                System.out.println("Nao ha ambiente destruivel");
                                break;
                            }

                            System.out.print("\r\nEntre com o nome do ambiente a destruir (ou ENTER para cancelar): ");
                            String oldNome = scanner.nextLine();

                            if (oldNome == null || oldNome.equals("")) {
                                pausar = false;
                            }
                            else if (destruiveis.contains(oldNome)) {
                                Ambiente oldAmb = new Ambiente();
                                oldAmb.nome = oldNome;
                                space.take(oldAmb, null, JavaSpace.NO_WAIT);
                                System.out.println("\r\nAmbiente " + oldNome + " destruido");
                            }
                            else {
                                System.out.println("\r\nAmbiente invalido");
                            }

                            destruiveis.clear();
                            break;
                        }

                        case "3":
                        {
                            Dispositivo novoDisp = new Dispositivo();
                            int dispIndex = 0;

                            while (true) {
                                dispIndex += 1;
                                String dispName = "disp" + dispIndex;
                                novoDisp.nome = dispName;
                                Dispositivo tempDisp = (Dispositivo) space.read(novoDisp, null, JavaSpace.NO_WAIT);

                                if (tempDisp == null) {
                                    space.write(novoDisp, null, Lease.FOREVER);
                                    System.out.println("\r\nDispositivo " + dispName + " criado");
                                    break;
                                }
                            }
                            break;
                        }

                        case "4":
                        {
                            List<Dispositivo> listaDisp = Helpers.listaDispositivo(space);
                            if (listaDisp.size() == 0) {
                                System.out.println("Nao ha dispositivos");
                                break;
                            }
                            List<String> destruiveis = new ArrayList<String>();

                            System.out.println("\r\nDispositivos destruiveis:");
                            for (int i = 0; i < listaDisp.size(); i++) {
                                String dispName = listaDisp.get(i).nome;

                                destruiveis.add(dispName);
                                System.out.println(dispName);
                            }
                            listaDisp.clear();

                            System.out.print("\r\nEntre com o nome do dispositivo a destruir (ou ENTER para cancelar): ");
                            String oldNome = scanner.nextLine();

                            if (oldNome == null || oldNome.equals("")) {
                                pausar = false;
                            }
                            else if (destruiveis.contains(oldNome)) {
                                Dispositivo oldDisp = new Dispositivo();
                                oldDisp.nome = oldNome;
                                space.take(oldDisp, null, JavaSpace.NO_WAIT);
                                System.out.println("\r\nDispositivo " + oldNome + " destruido");
                            }
                            else {
                                System.out.println("Dispositivo invalido");
                            }

                            destruiveis.clear();
                            break;
                        }

                        case "5":
                        {
                            List<Dispositivo> listaDisp = Helpers.listaDispositivo(space);
                            if (listaDisp.size() == 0) {
                                System.out.println("Nao ha dispositivos");
                                break;
                            }
                            Map<String, String> moviveis = new HashMap<String, String>();

                            System.out.println("\r\nDispositivos moviveis:");
                            for (int i = 0; i < listaDisp.size(); i++) {
                                Dispositivo disp = listaDisp.get(i);

                                moviveis.put(disp.nome, disp.amb);
                                System.out.println(disp.nome + " (" + disp.amb + ")");
                            }
                            listaDisp.clear();

                            System.out.print("\r\nEntre com o nome do dispositivo a mover (ou ENTER para cancelar): ");
                            String nome = scanner.nextLine();

                            if (nome == null || nome.equals("")) {
                                pausar = false;
                            }
                            else if (moviveis.containsKey(nome)) {
                                List<Ambiente> listaAmb = Helpers.listaAmbiente(space);
                                List<String> disponiveis = new ArrayList<String>();
                                String ambAtual = moviveis.get(nome);

                                System.out.println("\r\nAmbiente de destino disponiveis:");
                                for (int i = 0; i < listaAmb.size(); i++) {
                                    String ambName = listaAmb.get(i).nome;

                                    if (ambName.equals(ambAtual)) {
                                        continue;
                                    }

                                    disponiveis.add(ambName);
                                    System.out.println(ambName);
                                }
                                listaAmb.clear();

                                if (disponiveis.size() == 0) {
                                    System.out.println("Nao ha ambiente disponivel");
                                    moviveis.clear();
                                    break;
                                }

                                System.out.print("\r\nEntre com o ambiente de destino (ou ENTER para cancelar): ");
                                String novoAmb = scanner.nextLine();

                                if (novoAmb == null || novoAmb.equals("")) {
                                    pausar = false;
                                }
                                else if (novoAmb.equals(ambAtual)) {
                                    System.out.println ("\r\nDispositivo ja esta no ambiente " + ambAtual);
                                }
                                else if (disponiveis.contains(novoAmb)) {
                                    Dispositivo oldDisp = new Dispositivo();
                                    oldDisp.nome = nome;
                                    Dispositivo disp = (Dispositivo) space.take(oldDisp, null, JavaSpace.NO_WAIT);
                                    disp.amb = novoAmb;
                                    space.write(disp, null, Lease.FOREVER);
                                    System.out.println("\r\nDispositivo " + nome + " movido para o ambiente " + novoAmb);
                                }
                                else {
                                    System.out.println("\r\nAmbiente invalido");
                                }

                                disponiveis.clear();
                            }
                            else {
                                System.out.println("\r\nDispositivo invalido");
                            }

                            moviveis.clear();
                            break;
                        }
                        /*
                        case "6":
                        {
                            List<User> listaUser = Helpers.listaUsuario(space);
                            if (listaUser.size() == 0) {
                                System.out.println("Nao ha usuarios");
                                break;
                            }
                            Map<String, String> moviveis = new HashMap<String, String>();

                            System.out.println("\r\nUsuarios moviveis:");
                            for (int i = 0; i < listaUser.size(); i++) {
                                User user = listaUser.get(i);

                                moviveis.put(user.nome, user.amb);
                                System.out.println(user.nome + " (" + user.amb + ")");
                            }
                            listaUser.clear();

                            System.out.print("\r\nEntre com o nome do usuario a mover (ou ENTER para cancelar): ");
                            String nome = scanner.nextLine();

                            if (nome == null || nome.equals("")) {
                                pausar = false;
                            }
                            else if (moviveis.containsKey(nome)) {
                                List<Ambiente> listaAmb = Helpers.listaAmbiente(space);
                                List<String> disponiveis = new ArrayList<String>();
                                String ambAtual = moviveis.get(nome);

                                System.out.println("\r\nAmbiente de destino disponiveis:");
                                for (int i = 0; i < listaAmb.size(); i++) {
                                    String ambName = listaAmb.get(i).nome;

                                    if (ambName.equals(ambAtual)) {
                                        continue;
                                    }

                                    disponiveis.add(ambName);
                                    System.out.println(ambName);
                                }
                                listaAmb.clear();

                                if (disponiveis.size() == 0) {
                                    System.out.println("Nao ha ambiente disponivel");
                                    moviveis.clear();
                                    break;
                                }

                                System.out.print("\r\nEntre com o ambiente de destino (ou ENTER para cancelar): ");
                                String novoAmb = scanner.nextLine();

                                if (novoAmb == null || novoAmb.equals("")) {
                                    pausar = false;
                                }
                                else if (novoAmb.equals(ambAtual)) {
                                    System.out.println ("\r\nUsuario ja esta no ambiente " + ambAtual);
                                }
                                else if (disponiveis.contains(novoAmb)) {
                                    User oldUser = new User();
                                    oldUser.nome = nome;
                                    User user = (User) space.take(oldUser, null, JavaSpace.NO_WAIT);
                                    user.amb = novoAmb;
                                    space.write(user, null, Lease.FOREVER);
                                    System.out.println("\r\nUsuario " + nome + " movido para o ambiente " + novoAmb);
                                }
                                else {
                                    System.out.println("\r\nAmbiente invalido");
                                }

                                disponiveis.clear();
                            }
                            else {
                                System.out.println("\r\nUsuario invalido");
                            }

                            moviveis.clear();
                            break;
                        }
                        */
                        case "6":
                        {
                            List<Ambiente> listaAmb = Helpers.listaAmbiente(space);
                            if (listaAmb.size() == 0) {
                                System.out.println("Nao ha ambientes");
                                break;
                            }
                            
                            System.out.println("\r\nAmbientes encontrados:");
                            for (int i = 0; i < listaAmb.size(); i++) {
                                System.out.println(listaAmb.get(i).nome);
                            }
                            listaAmb.clear();

                            break;
                        }

                        case "7":
                        {
                            List<Dispositivo> listaNullDisp = Helpers.listaDispositivo(space, null);
                            if (listaNullDisp.size() > 0) {
                                System.out.println("\r\nDispositivos sem ambiente definido:");
                                for (int i = 0; i < listaNullDisp.size(); i++) {
                                    System.out.println(listaNullDisp.get(i).nome);
                                }
                                listaNullDisp.clear();
                            }
                            
                            List<Ambiente> listaAmb = Helpers.listaAmbiente(space);
                            List<String> pesquisaveis = new ArrayList<String>();
                            Dispositivo dispTemplate = new Dispositivo();
                            
                            System.out.println("\r\nAmbientes contendo dispositivos:");
                            for (int i = 0; i < listaAmb.size(); i++) {
                                String ambName = listaAmb.get(i).nome;

                                dispTemplate.amb = ambName;
                                Dispositivo tempDisp = (Dispositivo) space.read(dispTemplate, null, JavaSpace.NO_WAIT);
                                if (tempDisp == null) {
                                    continue;
                                }

                                pesquisaveis.add(ambName);
                                System.out.println(ambName);
                            }
                            listaAmb.clear();

                            if (pesquisaveis.size() == 0) {
                                System.out.println("Nao ha ambiente com dispositivo");
                                break;
                            }

                            System.out.print("\r\nEntre com o nome do ambiente a pesquisar (ou ENTER para cancelar): ");
                            String ambNome = scanner.nextLine();

                            if (ambNome == null || ambNome.equals("")) {
                                pausar = false;
                            }
                            else if (pesquisaveis.contains(ambNome)) {
                                List<Dispositivo> listaDisp = Helpers.listaDispositivo(space, ambNome);
                                
                                System.out.println("\r\nDispositivos encontrados no ambiente " + ambNome + ":");
                                for (int i = 0; i < listaDisp.size(); i++) {
                                    System.out.println(listaDisp.get(i).nome);
                                }
                                listaDisp.clear();
                            }
                            else {
                                System.out.println("\r\nAmbiente invalido");
                            }

                            pesquisaveis.clear();
                            break;
                        }

                        case "8":
                        {
                            List<User> listaNullUser = Helpers.listaUsuario(space, null);
                            if (listaNullUser.size() > 0) {
                                System.out.println("\r\nUsuarios sem ambiente definido:");
                                for (int i = 0; i < listaNullUser.size(); i++) {
                                    System.out.println(listaNullUser.get(i).nome);
                                }
                                listaNullUser.clear();
                            }

                            List<Ambiente> listaAmb = Helpers.listaAmbiente(space);
                            List<String> pesquisaveis = new ArrayList<String>();
                            User userTemplate = new User();
                            
                            System.out.println("\r\nAmbientes contendo usuarios:");
                            for (int i = 0; i < listaAmb.size(); i++) {
                                String ambName = listaAmb.get(i).nome;

                                userTemplate.amb = ambName;
                                User tempUser = (User) space.read(userTemplate, null, JavaSpace.NO_WAIT);
                                if (tempUser == null) {
                                    continue;
                                }

                                pesquisaveis.add(ambName);
                                System.out.println(ambName);
                            }
                            listaAmb.clear();

                            if (pesquisaveis.size() == 0) {
                                System.out.println("Nao ha ambiente com usuario");
                                break;
                            }

                            System.out.print("\r\nEntre com o nome do ambiente a pesquisar (ou ENTER para cancelar): ");
                            String ambNome = scanner.nextLine();

                            if (ambNome == null || ambNome.equals("")) {
                                pausar = false;
                            }
                            else if (pesquisaveis.contains(ambNome)) {
                                List<User> listaUser = Helpers.listaUsuario(space, ambNome);
                                
                                System.out.println("\r\nUsuarios encontrados no ambiente " + ambNome + ":");
                                for (int i = 0; i < listaUser.size(); i++) {
                                    System.out.println(listaUser.get(i).nome);
                                }
                                listaUser.clear();
                            }
                            else {
                                System.out.println("\r\nAmbiente invalido");
                            }

                            pesquisaveis.clear();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
