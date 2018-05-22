import net.jini.space.JavaSpace;
import net.jini.core.lease.Lease;

import java.util.*;

public class Helpers {

    public static List<Ambiente> listaAmbiente(JavaSpace space) throws Exception {
        List<Ambiente> listaAmb = new ArrayList<Ambiente>();
        Ambiente template = new Ambiente();
        Ambiente amb;

        do {
            amb = (Ambiente) space.take(template, null, JavaSpace.NO_WAIT);

            if (amb != null) {
                listaAmb.add(amb);
            }
        } while (amb != null);

        for (int i = 0; i < listaAmb.size(); i++) {
            amb = listaAmb.get(i);
            space.write(amb, null, Lease.FOREVER);
        }

        return listaAmb;
    }

    public static List<Dispositivo> listaDispositivo(JavaSpace space) throws Exception {
        return listaDispositivo(space, "");
    }

    public static List<Dispositivo> listaDispositivo(JavaSpace space, String amb) throws Exception {
        List<Dispositivo> listaDisp= new ArrayList<Dispositivo>();
        Dispositivo template = new Dispositivo();
        Dispositivo disp;

        if (amb != "" ) {
            template.amb = amb;
        }

        do {
            disp = (Dispositivo) space.take(template, null, JavaSpace.NO_WAIT);

            if (disp != null) {
                listaDisp.add(disp);
            }
        } while (disp != null);

        for (int i = listaDisp.size()-1; i >= 0; i--) {
            disp = listaDisp.get(i);
            space.write(disp, null, Lease.FOREVER);
            
            if (amb == null && disp.amb != null) {
                listaDisp.remove(disp);
            }
        }
        
        return listaDisp;
    }

    public static List<User> listaUsuario(JavaSpace space) throws Exception {
        return listaUsuario(space, "");
    }

    public static List<User> listaUsuario(JavaSpace space, String amb) throws Exception {
        List<User> listaUser= new ArrayList<User>();
        User template = new User();
        User user;

        if (amb != "" ) {
            template.amb = amb;
        }

        do {
            user = (User) space.take(template, null, JavaSpace.NO_WAIT);

            if (user != null) {
                listaUser.add(user);
            }
        } while (user != null);

        for (int i = listaUser.size()-1; i >= 0; i--) {
            user = listaUser.get(i);
            space.write(user, null, Lease.FOREVER);

            if (amb == null && user.amb != null) {
                listaUser.remove(user);
            }
        }

        return listaUser;
    }

}
