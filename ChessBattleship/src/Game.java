
/**
 * Descripción: Este programa consta de las clases Tar708, NuevoUsuario, OlvideContras y JuegoUnJugador.
 *
 * Clase Game: Interfaz gráfica de un Log-In. Guarda y lee la información de los usuarios por medio de archivos.
 * Si el usuario y la contraseña son correctos, se invoca a JuegoUnJugador; sino, se puede crear el usuario mediante
 * la clase NuevoUsuario o reestablecer su contraseña mediante la clase OlvideContras.
 *
 * Clase NuevoUsuario: Interfaz gráfica de un registro de usuario. Captura username, contraseña, nombre desde el teclado;
 * por medio de un checklist obtiene su género e inserta su año de nacimiento desde un List. Cuenta con las opciones de registrar, borrar y cancealr.
 * Si el usuario ya existe, se notificará que el usuario existe. Si no ha llenado todos los campos, no puede guardar el archivo de registro de usuario.
 * La información la guarda con un formato de username|contraseña|nombre|genero|año de nacimiento.
 *
 * Clase OlvideContras: Interfaz gráfica de cambiar contraseña si el usuario olvidó la suya. En caso de que el nombre de usuario no exista, se notificará que no existe.
 * Si no hay usuarios registrados, se notificará que no hay algún archivo con la información para cambiar.
 *
 * Clase JuegoUnJugador: Interfaz gráfica que ofrece un tablero de ajedrez con posiciones aleatorias para el jugador y la máquina. El usuario puede ver sus piezas y
 * no las de la computadora. El usuario debe ingresar la fila y columna en los TextFields correspondientes y presionar el botón de "¡Ataca!" para atacar la casilla seleccionada.
 * Si le atina a una pieza rival, se le sumará un valor a su score y se indicará que una pieza fue atacada; sino, la casilla se pondrá en gris. Espera para atacar hasta que pase el turno de la computadora.
 * Existen las opciones de pantalla (modo claro y oscuro), guardar la posición en un nuevo archivo y leer la posición de dicho archivo, reconociendo el puntaje y casillas atacadas.
 * También hay un reloj programado con multi-hilos que coloca el tiempo que ha tomado el juego en formato minutos:segundos, aunque este no tiene límite.
 *
 */

/*
 * @author G-Chelius-A.
 *
 * 28/10/2024
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import javax.swing.JOptionPane;

//Interfaz gráfica del menú del LogIn.
public class Game extends Frame implements ActionListener {

    Label lTitulo, lUser, lPassword, lSub, lAux1;
    Button bInicia, bNuevaCuenta, bResetContra, bSalir;
    TextField tUser, tPass;

    File f;

    public static String userName = "NoName";

    Font fCommonText = new Font("Arial", Font.PLAIN, 20);
    Font fTitulo = new Font("Monospaced", Font.BOLD, 30);
    Font fCommonButton = new Font("Arial", Font.BOLD, 12);

    public Game() {
        super("Tar708. Batalla Naval de Ajedrez");
        f = new File("Tar708Users.txt");
        setLayout(null);
        setVisible(true);
        setResizable(false);
        setSize(650, 550);
        setBackground(Color.BLACK);
        this.elementosIGU();
    }

    public void elementosIGU() {

        lTitulo = new Label("BATALLA NAVAL");
        lTitulo.setBounds(200, 100, 260, 50);
        lTitulo.setFont(fTitulo);
        lTitulo.setBackground(Color.BLACK);
        lTitulo.setAlignment(1);
        lTitulo.setForeground(Color.WHITE);
        this.add(lTitulo);

        lSub = new Label("De Ajedrez");
        lSub.setBounds(230, 150, 200, 30);
        lSub.setAlignment(1);
        lSub.setBackground(Color.YELLOW);
        lSub.setForeground(Color.BLACK);
        lSub.setFont(fTitulo);
        this.add(lSub);

        lUser = new Label("Usuario:");
        lUser.setBounds(75, 200, 100, 50);
        lUser.setFont(fCommonText);
        lUser.setBackground(Color.getHSBColor(60, 102, 255));
        this.add(lUser);

        lPassword = new Label("Contraseña:");
        lPassword.setBounds(75, 250, 120, 50);
        lPassword.setBackground(Color.getHSBColor(60, 102, 255));
        lPassword.setFont(fCommonText);
        this.add(lPassword);

        tUser = new TextField("");
        tUser.setBounds(220, 212, 270, 30);
        tUser.setFont(fCommonText);
        this.add(tUser);

        tPass = new TextField("");
        tPass.setBounds(220, 260, 270, 30);
        tPass.setFont(fCommonText);
        this.add(tPass);

        bInicia = new Button("Iniciar sesión");
        bInicia.setBounds(50 + 30, 350, 120, 50);
        bInicia.setFont(fCommonButton);
        bInicia.addActionListener(this);
        bInicia.setBackground(Color.WHITE);
        this.add(bInicia);

        bNuevaCuenta = new Button("Crear cuenta");
        bNuevaCuenta.setBounds(175 + 30, 350, 120, 50);
        bNuevaCuenta.setFont(fCommonButton);
        bNuevaCuenta.setBackground(Color.WHITE);
        bNuevaCuenta.addActionListener(this);
        this.add(bNuevaCuenta);

        bResetContra = new Button("Olvidé contraseña");
        bResetContra.setBounds(175 + 125 + 30, 350, 120, 50);
        bResetContra.setFont(fCommonButton);
        bResetContra.setBackground(Color.WHITE);
        bResetContra.addActionListener(this);
        this.add(bResetContra);

        bSalir = new Button("Salir");
        bSalir.setBounds(175 + 125 * 2 + 30, 350, 120, 50);
        bSalir.setFont(fCommonButton);
        bSalir.addActionListener(this);
        bSalir.setBackground(Color.WHITE);
        this.add(bSalir);

        lAux1 = new Label("");
        lAux1.setBounds(20, 40, 610, 490);
        lAux1.setBackground(Color.getHSBColor(60, 102, 255));
        this.add(lAux1);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bInicia) {
            validaContra(tUser.getText(), tPass.getText());
        }

        if (e.getSource() == bNuevaCuenta) {
            NuevoUsuario newUser = new NuevoUsuario();
            this.setVisible(false);
        }

        if (e.getSource() == bResetContra) {
            OlvideContras oc = new OlvideContras();
            this.setVisible(false);
        }

        if (e.getSource() == bSalir) {
            System.exit(0);
        }
    }

    public void validaContra(String user, String psw) {
        String aux;
        try {
            if (!f.exists()) {
                JOptionPane.showMessageDialog(null, "Regístrese primero.", "Error", 0);
                return;
            }
            BufferedReader br = new BufferedReader(new FileReader(f));
            while (true) {
                aux = br.readLine();
                if (aux == null) {
                    JOptionPane.showMessageDialog(null, "Contraseña o usuario incorrectos. Inténtelo de nuevo.", "", -1);
                    break;
                }
                String datos[] = aux.split("\\|");
                String validateUser = datos[0];
                String validatePsw = datos[1];
                String genero = datos[3];
                String msg;

                if (genero.equals("Masculino")) {
                    msg = "Bienvenido";
                } else {
                    if (genero.equals("Femenino")) {
                        msg = "Bienvenida";
                    } else {
                        msg = "Hola";
                    }
                }

                if (user.equals(validateUser) && psw.equals(validatePsw)) {
                    Game.userName = user;
                    JOptionPane.showMessageDialog(null, msg + " de nuevo, " + tUser.getText(), "", -1);
                    this.setVisible(false);
                    JuegoUnJugador xx = new JuegoUnJugador();
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error en el archivo: " + e, "Error", 0);
        }
    }
}

//Interfaz gráfica para la opción "Crear cuenta".
class NuevoUsuario extends Frame implements ActionListener {

    Label lTitulo, lUser, lPassword, lNombre, lGenero, lNac, lAux1; //Nacimiento de 1940 a 2024.
    Button bRegistra, bCancelar, bBorrar;
    TextField tUser, tPass, tNom;
    CheckboxGroup generos;
    Checkbox hombre, mujer, otro;
    Choice years;

    File f;

    Font fCommonText = new Font("Arial", Font.PLAIN, 20);
    Font fTitulo = new Font("Monospaced", Font.BOLD, 20);
    Font fCommonButton = new Font("Arial", Font.BOLD, 12);
    Font fChoice = new Font("Arial", Font.BOLD, 15);

    public NuevoUsuario() {
        super("Tar708. Nuevo usuario");
        f = new File("Tar708Users.txt");
        setLayout(null);
        setVisible(true);
        setResizable(false);
        setSize(650, 550);
        setBackground(Color.BLACK);
        this.elementosIGU();
    }

    public void elementosIGU() {
        lTitulo = new Label("Registrar Usuario");
        lTitulo.setBounds(200, 100, 260, 50);
        lTitulo.setAlignment(1);
        lTitulo.setBackground(Color.BLUE);
        lTitulo.setForeground(Color.WHITE);
        lTitulo.setFont(fTitulo);
        this.add(lTitulo);

        lUser = new Label("Usuario:");
        lUser.setBounds(75, 200 - 50, 100, 50);
        lUser.setFont(fCommonText);
        lUser.setBackground(Color.getHSBColor(60, 102, 255));
        this.add(lUser);

        lPassword = new Label("Contraseña:");
        lPassword.setBounds(75, 250 - 50, 120, 50);
        lPassword.setFont(fCommonText);
        lPassword.setBackground(Color.getHSBColor(60, 102, 255));
        this.add(lPassword);

        lNombre = new Label("Nombre:");
        lNombre.setBounds(75, 300 - 50, 100, 50);
        lNombre.setFont(fCommonText);
        lNombre.setBackground(Color.getHSBColor(60, 102, 255));
        this.add(lNombre);

        lGenero = new Label("Género:");
        lGenero.setBounds(75, 350 - 50, 100, 50);
        lGenero.setFont(fCommonText);
        lGenero.setBackground(Color.getHSBColor(60, 102, 255));
        this.add(lGenero);

        lNac = new Label("Año de nacimiento:");
        lNac.setBounds(275, 350 - 50, 200, 50);
        lNac.setFont(fCommonText);
        lNac.setBackground(Color.getHSBColor(60, 102, 255));
        this.add(lNac);

        tUser = new TextField("");
        tUser.setBounds(220, 212 - 50, 270, 30);
        tUser.setFont(fCommonText);
        this.add(tUser);

        tPass = new TextField("");
        tPass.setBounds(220, 260 - 50, 270, 30);
        tPass.setFont(fCommonText);
        this.add(tPass);

        tNom = new TextField("");
        tNom.setBounds(220, 308 - 50, 270, 30);
        tNom.setFont(fCommonText);
        this.add(tNom);

        generos = new CheckboxGroup();
        hombre = new Checkbox("Masculino", generos, false);
        hombre.setBounds(75, 400 - 50, 100, 25);
        hombre.setFont(fChoice);
        hombre.setBackground(Color.getHSBColor(60, 102, 255));
        this.add(hombre);
        mujer = new Checkbox("Femenino", generos, false);
        mujer.setBounds(75, 430 - 50, 100, 25);
        mujer.setFont(fChoice);
        mujer.setBackground(Color.getHSBColor(60, 102, 255));
        this.add(mujer);
        otro = new Checkbox("Otro", generos, false);
        otro.setBounds(75, 460 - 50, 100, 25);
        otro.setFont(fChoice);
        otro.setBackground(Color.getHSBColor(60, 102, 255));
        this.add(otro);

        years = new Choice();
        years.setBounds(300, 400 - 50, 110, 70);
        years.add("Seleccione");
        for (int i = 1940; i <= 2024; i++) {
            years.add(Integer.toString(i));
        }
        years.setFont(fChoice);
        this.add(years);

        bRegistra = new Button("Registrar");
        bRegistra.setBounds(200 - 100, 460, 120, 50);
        bRegistra.setFont(fCommonButton);
        bRegistra.addActionListener(this);
        bRegistra.setBackground(Color.WHITE);
        this.add(bRegistra);

        bBorrar = new Button("Borrar");
        bBorrar.setBounds(350 - 100, 460, 120, 50);
        bBorrar.setFont(fCommonButton);
        bBorrar.addActionListener(this);
        bBorrar.setBackground(Color.WHITE);
        this.add(bBorrar);

        bCancelar = new Button("Cancelar");
        bCancelar.setBounds(500 - 100, 460, 120, 50);
        bCancelar.setFont(fCommonButton);
        bCancelar.addActionListener(this);
        bCancelar.setBackground(Color.WHITE);
        this.add(bCancelar);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        lAux1 = new Label("");
        lAux1.setBounds(20, 40, 610, 490);
        lAux1.setBackground(Color.getHSBColor(60, 102, 255));
        this.add(lAux1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bRegistra) {
            String user;
            String psw;
            String nom;

            user = tUser.getText();
            psw = tPass.getText();
            nom = tNom.getText();

            if (f.exists() && !obtenInfoUsuario(user)) {
                JOptionPane.showMessageDialog(null, "Ese usuario ya existe. Intente otro nombre.", "", -1);
            } else {
                try {
                    if (psw.equals("") || "Seleccione".equals(years.getSelectedItem())) {
                        throw new NullPointerException();
                    }
                    String linea = user + "|" + psw + "|" + nom + "|" + generos.getSelectedCheckbox().getLabel() + "|" + years.getSelectedItem();
                    grabaArchivo(linea);
                    bRegistra.setEnabled(false);
                    JOptionPane.showMessageDialog(null, "Cuenta creada.", "", -1);
                    this.setVisible(false);
                    Game myTar = new Game();
                } catch (NullPointerException except) {
                    JOptionPane.showMessageDialog(null, "Llene todos los campos.", "", 1);
                }
            }
        }

        if (e.getSource() == bBorrar) {
            try {
                tUser.setText("");
                tPass.setText("");
                tNom.setText("");

                years.select(0);

                //Resetear Checkbox.
                Checkbox actual = generos.getCurrent();
                generos.setCurrent(null);
                actual.setState(false);

            } catch (NullPointerException nullpoint) {

            }
        }

        if (e.getSource() == bCancelar) {
            this.setVisible(false);
            Game myTar = new Game();
        }
    }

    public void creaArchivo() {
        try {
            f.createNewFile();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "El archivo ya existe.", "Error", 0);
        }
    }

    public void grabaArchivo(String linea) {
        try {
            //f.delete();
            if (!f.exists()) {
                creaArchivo();
            }
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, true), "utf-8"));
            bw.write(linea + "\r\n");
            bw.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error en el archivo: " + e, "Error", 0);
        }
    }

    public boolean obtenInfoUsuario(String user) {
        String aux;
        int iteraciones = 0;
        boolean valor = false;
        try {
            if (!f.exists()) {
                JOptionPane.showMessageDialog(null, "No hay ningún usuario registrado.", "Error", 0);
            }
            BufferedReader br = new BufferedReader(new FileReader(f));
            while (true) {
                aux = br.readLine();
                if (aux == null) {
                    break;
                }
                String lineaAReemplazar = aux;
                String datos[] = aux.split("\\|");
                String validateUser = datos[0];

                if (user.equals(validateUser)) {
                    return false;
                }
            }
        } catch (IOException e) {
            //JOptionPane.showMessageDialog(null, "Error en el archivo: " + e, "Error", 0);
        }
        return true;
    }

}

//Interfaz gráfica para la opción "Olvidé mi contraseña".
class OlvideContras extends Frame implements ActionListener {

    Label lTitulo, lUser, lPassword, lPassw2, lAux1;
    Button bEstablece, bCancelar, bBorrar;
    TextField tUser, tPass, tPass2;

    File f, g;

    Font fCommonText = new Font("Arial", Font.PLAIN, 20);
    Font fTitulo = new Font("Monospaced", Font.BOLD, 20);
    Font fCommonButton = new Font("Arial", Font.BOLD, 12);

    public OlvideContras() {
        super("Tar708. Olvidé contraseña");
        f = new File("Tar708Users.txt");
        g = new File("aux.txt");
        setLayout(null);
        setVisible(true);
        setResizable(false);
        setSize(650, 550);
        setBackground(Color.BLACK);
        this.elementosIGU();
    }

    public void elementosIGU() {
        lTitulo = new Label("Cambiar contraseña");
        lTitulo.setBounds(200, 100, 260, 50);
        lTitulo.setFont(fTitulo);
        lTitulo.setBackground(Color.RED);
        lTitulo.setForeground(Color.WHITE);
        lTitulo.setAlignment(1);
        this.add(lTitulo);

        lUser = new Label("Usuario:");
        lUser.setBounds(75, 200, 100, 50);
        lUser.setFont(fCommonText);
        lUser.setBackground(Color.getHSBColor(60, 102, 255));
        this.add(lUser);

        lPassword = new Label("Contraseña:");
        lPassword.setBounds(75, 250, 120, 50);
        lPassword.setBackground(Color.getHSBColor(60, 102, 255));
        lPassword.setFont(fCommonText);
        this.add(lPassword);

        lPassw2 = new Label("Contraseña:");
        lPassw2.setBounds(75, 300, 120, 50);
        lPassw2.setFont(fCommonText);
        lPassw2.setBackground(Color.getHSBColor(60, 102, 255));
        this.add(lPassw2);

        tUser = new TextField("");
        tUser.setBounds(220, 212, 270, 30);
        tUser.setFont(fCommonText);
        this.add(tUser);

        tPass = new TextField("");
        tPass.setBounds(220, 260, 270, 30);
        tPass.setFont(fCommonText);
        this.add(tPass);

        tPass2 = new TextField("");
        tPass2.setBounds(220, 308, 270, 30);
        tPass2.setFont(fCommonText);
        this.add(tPass2);

        bEstablece = new Button("Restablecer");
        bEstablece.setBounds(200 - 100, 450, 120, 50);
        bEstablece.setFont(fCommonButton);
        bEstablece.addActionListener(this);
        bEstablece.setBackground(Color.WHITE);
        this.add(bEstablece);

        bBorrar = new Button("Borrar");
        bBorrar.setBounds(350 - 100, 450, 120, 50);
        bBorrar.setFont(fCommonButton);
        bBorrar.addActionListener(this);
        bBorrar.setBackground(Color.WHITE);
        this.add(bBorrar);

        bCancelar = new Button("Cancelar");
        bCancelar.setBounds(500 - 100, 450, 120, 50);
        bCancelar.setFont(fCommonButton);
        bCancelar.addActionListener(this);
        bCancelar.setBackground(Color.WHITE);
        this.add(bCancelar);

        lAux1 = new Label("");
        lAux1.setBounds(20, 40, 610, 490);
        lAux1.setBackground(Color.getHSBColor(60, 102, 255));
        this.add(lAux1);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bEstablece) {
            String user;
            String psw;
            String psw2;

            user = tUser.getText();
            psw = tPass.getText();
            psw2 = tPass2.getText();

            if (psw2.equals(psw)) {
                obtenInfoUsuario(user, psw);
                bEstablece.setEnabled(false);
                this.setVisible(false);
                Game myTar = new Game();
            } else {
                JOptionPane.showMessageDialog(null, "Haga que la contraseña coincida.", "", -1);
            }
        }

        if (e.getSource() == bBorrar) {
            try {
                tUser.setText("");
                tPass.setText("");
                tPass2.setText("");

            } catch (NullPointerException nullpoint) {

            }
        }

        if (e.getSource() == bCancelar) {
            this.setVisible(false);
            Game myTar = new Game();
        }
    }

    public void creaArchivo(File arch) {
        try {
            arch.createNewFile();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "El archivo ya existe.", "Error", 0);
        }
    }

    public void obtenInfoUsuario(String user, String psw) {
        String aux;
        int limit = obtenNumeroRegistros(f);
        int iteraciones = 0;
        boolean valor = false;
        try {
            if (!f.exists()) {
                JOptionPane.showMessageDialog(null, "No hay ningún usuario registrado.", "Error", 0);
            }
            BufferedReader br = new BufferedReader(new FileReader(f));
            while (true) {
                aux = br.readLine();
                if (aux == null) {
                    break;
                }
                String lineaAReemplazar = aux;
                String datos[] = aux.split("\\|");
                String validateUser = datos[0];

                if (user.equals(validateUser)) {
                    String nom = datos[2];
                    String genero = datos[3];
                    String year = datos[4];
                    lineaAReemplazar = lineaAReemplazar.replace(aux, validateUser + "|" + psw + "|" + nom + "|" + genero + "|" + year + "\r\n");

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(g, true), "utf-8"));
                    bw.write(lineaAReemplazar);
                    bw.close();

                    JOptionPane.showMessageDialog(null, "Contraseña reestablecida.", "", -1);
                } else {
                    grabaArchivo(aux, g);
                    iteraciones++;
                    if (iteraciones >= limit) {
                        JOptionPane.showMessageDialog(null, "El usuario no existe.", "", -1);
                    }
                }
            }

            f.delete();
            creaArchivo(f);
            OutputStream os = new FileOutputStream(f);
            Files.copy(Paths.get(g.getPath()), os);
            g.delete();
        } catch (IOException e) {
            //JOptionPane.showMessageDialog(null, "Error en el archivo: " + e, "Error", 0);
        }
    }

    public void grabaArchivo(String linea, File arch) {
        try {
            //f.delete();
            if (!arch.exists()) {
                creaArchivo(arch);
            }
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arch, true), "utf-8"));
            bw.write(linea + "\r\n");
            bw.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error en el archivo: " + e, "Error", 0);
        }
    }

    public int obtenNumeroRegistros(File f) {
        String aux;
        int i = 0;
        try {
            FileReader r = new FileReader(f);
            BufferedReader br = new BufferedReader(r);
            while (true) {
                aux = br.readLine();
                if (aux == null) {
                    break;
                }
                i++;
            }
            br.close();

        } catch (IOException e) {

        }
        return i;
    }
}

/*
================================================================================

                        ESTO YA ES DEL JUEGO EN SÍ

================================================================================
 */
class JuegoUnJugador extends Frame implements ActionListener, Runnable {

    int scoreKing = 100, scoreQueen = 10, scoreRook = 5, scoreKnight = 2, scoreBishop = 3, scorePawn = 1;
    int anchoTablero = 30;
    int score1, score2;
    static int[][] tablero1 = new int[8][8];
    int[][] tablero2 = new int[8][8];
    static Label[][] lTablero1 = new Label[8][8];
    Label[] lEtFila1 = new Label[8];
    Label[] lEtColumna1 = new Label[8];
    Label[] lEtFila2 = new Label[8];
    Label[] lEtColumna2 = new Label[8];
    Boolean partidaAcabada = false;
    Label[][] lTablero2 = new Label[8][8];
    Label lJug1, lJug2, lFila, lColumna, lTiempo, lPuntaje, lAux1;
    Button bGuarda, bCarga, bSalir, bReset, bLanza;
    TextField tPuntaje, tFila, tColumna;
    List modoPantalla;

    File f;

    Font piezas = new Font("monospaced", Font.BOLD, 20);
    Font labels = new Font("Arial", Font.BOLD, 15);
    Font fTiempo = new Font("monospaced", Font.BOLD, 25);
    Font fButton = new Font("Arial", Font.BOLD, 15);

    Thread tempo = new Thread(this, "Tiempo");

    public JuegoUnJugador() {
        super("Tar 708. Batalla Naval de Ajedrez");
        f = new File(Game.userName + "data.txt");
        setLayout(null);
        setVisible(true);
        setResizable(false);
        setSize(650, 550);
        setBackground(Color.BLACK);
        this.iniciaTablero();
        this.asignaCasillasComputadora(tablero1);
        this.imprimeCaracteres(tablero1, lTablero1, true);
        this.asignaCasillasComputadora(tablero2);
        //this.imprimeCaracteres(tablero2, lTablero2);
        this.elementosIGU();
        tempo.start();

    }

    public void elementosIGU() {

        lJug1 = new Label(Game.userName);
        lJug1.setBounds(25, 110, 30 * 8, 30);
        lJug1.setFont(labels);
        lJug1.setAlignment(1);
        lJug1.setBackground(Color.RED);
        lJug1.setForeground(Color.WHITE);
        this.add(lJug1);

        lJug2 = new Label("PC");
        lJug2.setBounds(390, 110, 30 * 8, 30);
        lJug2.setFont(labels);
        lJug2.setAlignment(1);
        lJug2.setBackground(Color.BLUE);
        lJug2.setForeground(Color.WHITE);
        this.add(lJug2);

        lFila = new Label("Fila");
        lFila.setBounds(225, 405, 70, 25);
        lFila.setFont(labels);
        lFila.setAlignment(1);
        lFila.setBackground(Color.WHITE);
        this.add(lFila);

        lColumna = new Label("Columna");
        lColumna.setBounds(350, 405, 70, 25);
        lColumna.setFont(labels);
        lColumna.setAlignment(1);
        lColumna.setBackground(Color.WHITE);
        this.add(lColumna);

        lPuntaje = new Label("Puntaje");
        lPuntaje.setBounds(290, 220, 70, 30);
        lPuntaje.setFont(labels);
        lPuntaje.setAlignment(1);
        lPuntaje.setBackground(Color.GREEN);
        lPuntaje.setForeground(Color.BLACK);
        this.add(lPuntaje);

        lTiempo = new Label("0:00");
        lTiempo.setBounds(280, 65, 100, 50);
        lTiempo.setFont(fTiempo);
        lTiempo.setAlignment(1);
        lTiempo.setBackground(Color.GREEN);
        lTiempo.setForeground(Color.BLACK);
        this.add(lTiempo);

        bSalir = new Button("Salir");
        bSalir.setBounds(520, 60, 75, 30);
        bSalir.setFont(fButton);
        bSalir.addActionListener(this);
        bSalir.setBackground(Color.WHITE);
        this.add(bSalir);

        bLanza = new Button("¡Ataca!");
        bLanza.setBounds(280, 480, 80, 35);
        bLanza.addActionListener(this);
        bLanza.setBackground(Color.RED);
        bLanza.setForeground(Color.WHITE);
        bLanza.setFont(new Font("monospaced", Font.BOLD, 17));
        this.add(bLanza);

        bGuarda = new Button("Guarda");
        bGuarda.setBounds(75, 480, 75, 30);
        bGuarda.addActionListener(this);
        bGuarda.setBackground(Color.WHITE);
        this.add(bGuarda);

        bCarga = new Button("Carga");
        bCarga.setBounds(165, 480, 75, 30);
        bCarga.addActionListener(this);
        bCarga.setBackground(Color.WHITE);
        this.add(bCarga);

        bReset = new Button("Rendirse");
        bReset.setBounds(400, 480, 75, 30);
        bReset.addActionListener(this);
        bReset.setBackground(Color.WHITE);
        this.add(bReset);

        tPuntaje = new TextField("0");
        tPuntaje.setBounds(285, 260, 75, 30);
        tPuntaje.setBackground(Color.WHITE);
        tPuntaje.setEditable(false);
        tPuntaje.setFont(new Font("monospaced", Font.ROMAN_BASELINE, 20));
        this.add(tPuntaje);

        tFila = new TextField("");
        tFila.setBounds(222, 435, 75, 30);
        tFila.setBackground(Color.WHITE);
        tFila.setFont(new Font("monospaced", Font.ROMAN_BASELINE, 20));
        this.add(tFila);

        tColumna = new TextField("");
        tColumna.setBounds(350, 435, 75, 30);
        tColumna.setBackground(Color.WHITE);
        tColumna.setFont(new Font("monospaced", Font.ROMAN_BASELINE, 20));
        this.add(tColumna);

        modoPantalla = new List();
        modoPantalla.addActionListener(this);
        modoPantalla.setBounds(50, 60, 90, 40);
        modoPantalla.setBackground(Color.WHITE);
        modoPantalla.setFont(new Font("Arial", Font.PLAIN, 13));
        modoPantalla.add("Modo claro");
        modoPantalla.add("Modo oscuro");
        add(modoPantalla);

        for (int i = 0; i < lEtFila1.length; i++) {
            lEtFila1[i] = new Label(Integer.toString(i + 1));
            lEtFila1[i].setAlignment(1);
            lEtFila1[i].setBounds(35 + anchoTablero * i, 150, anchoTablero, 10);
            lEtFila1[i].setFont(new Font("monospaced", Font.BOLD, 10));
            lEtFila1[i].setBackground(Color.CYAN);
            lEtFila1[i].setForeground(Color.BLACK);
            this.add(lEtFila1[i]);
        }

        for (int i = 0; i < lEtColumna1.length; i++) {
            lEtColumna1[i] = new Label(Integer.toString(i + 1));
            lEtColumna1[i].setAlignment(1);
            lEtColumna1[i].setBounds(25, 160 + anchoTablero * i, 10, anchoTablero);
            lEtColumna1[i].setFont(new Font("monospaced", Font.BOLD, 10));
            lEtColumna1[i].setBackground(Color.CYAN);
            lEtColumna1[i].setForeground(Color.BLACK);
            this.add(lEtColumna1[i]);
        }

        for (int i = 0; i < lEtFila2.length; i++) {
            lEtFila2[i] = new Label(Integer.toString(i + 1));
            lEtFila2[i].setAlignment(1);
            lEtFila2[i].setBounds(380 + anchoTablero * i, 150, anchoTablero, 10);
            lEtFila2[i].setFont(new Font("monospaced", Font.BOLD, 10));
            lEtFila2[i].setBackground(Color.CYAN);
            lEtFila2[i].setForeground(Color.BLACK);
            this.add(lEtFila2[i]);
        }

        for (int i = 0; i < lEtColumna2.length; i++) {
            lEtColumna2[i] = new Label(Integer.toString(i + 1));
            lEtColumna2[i].setAlignment(1);
            lEtColumna2[i].setBounds(620, 160 + anchoTablero * i, 10, anchoTablero);
            lEtColumna2[i].setFont(new Font("monospaced", Font.BOLD, 10));
            lEtColumna2[i].setBackground(Color.CYAN);
            lEtColumna2[i].setForeground(Color.BLACK);
            this.add(lEtColumna2[i]);
        }

        lAux1 = new Label("");
        lAux1.setBounds(15, 40, 620, 490);
        lAux1.setBackground(Color.getHSBColor(60, 102, 255));
        this.add(lAux1);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void iniciaTablero() {
        for (int i = 0; i < tablero1.length; i++) {
            for (int j = 0; j < tablero1[0].length; j++) {
                tablero1[i][j] = 0;
                tablero1[i][j] = 0;
                lTablero1[i][j] = new Label("");
                lTablero1[i][j].setBounds(35 + anchoTablero * i, 160 + anchoTablero * j, anchoTablero, anchoTablero);
                if ((i + 1) % 2 == 0) {
                    if ((j + 1) % 2 == 0) {
                        lTablero1[i][j].setBackground(Color.WHITE);
                    } else {
                        lTablero1[i][j].setBackground(Color.BLACK);
                        lTablero1[i][j].setForeground(Color.YELLOW);
                    }
                } else {
                    if ((j + 1) % 2 == 0) {
                        lTablero1[i][j].setBackground(Color.BLACK);
                        lTablero1[i][j].setForeground(Color.YELLOW);
                    } else {
                        lTablero1[i][j].setBackground(Color.WHITE);

                    }
                }
                lTablero1[i][j].setFont(piezas);
                lTablero1[i][j].setAlignment(1);
                this.add(lTablero1[i][j]);

                tablero2[i][j] = 0;
                tablero2[i][j] = 0;
                lTablero2[i][j] = new Label("");
                lTablero2[i][j].setBounds(380 + anchoTablero * i, 160 + anchoTablero * j, anchoTablero, anchoTablero);
                if ((i + 1) % 2 == 0) {
                    if ((j + 1) % 2 == 0) {
                        lTablero2[i][j].setBackground(Color.WHITE);

                    } else {
                        lTablero2[i][j].setBackground(Color.BLACK);
                        lTablero2[i][j].setForeground(Color.YELLOW);
                    }
                } else {
                    if ((j + 1) % 2 == 0) {
                        lTablero2[i][j].setBackground(Color.BLACK);
                        lTablero2[i][j].setForeground(Color.YELLOW);
                    } else {
                        lTablero2[i][j].setBackground(Color.WHITE);
                    }

                }
                lTablero2[i][j].setFont(piezas);
                lTablero2[i][j].setAlignment(1);
                this.add(lTablero2[i][j]);

            }
        }
    }

    public int generaFilaColumna() {
        int num = (int) (Math.random() * (tablero1.length + 1));
        if (num >= tablero1.length) {
            num = tablero1.length - 1;
        }
        return num;
    }

    public void asignaCasillasComputadora(int[][] tablero2) {
        int king = 1;
        int queen = 1;
        int rook = 2;
        int bishop = 2;
        int knight = 2;
        int pawn = 8;
        int x = 0, y = 0;

        int i = 0;

        while (i < king) {
            x = generaFilaColumna();
            y = generaFilaColumna();

            if (tablero2[x][y] == 0) {
                tablero2[x][y] = scoreKing;
                i++;
            }
        }
        i = 0;

        while (i < queen) {
            x = generaFilaColumna();
            y = generaFilaColumna();
            if (tablero2[x][y] == 0) {
                tablero2[x][y] = scoreQueen;
                i++;
            }
        }
        i = 0;

        while (i < rook) {
            x = generaFilaColumna();
            y = generaFilaColumna();

            if (tablero2[x][y] == 0) {
                tablero2[x][y] = scoreRook;
                i++;
            }
        }
        i = 0;

        while (i < bishop) {
            x = generaFilaColumna();
            y = generaFilaColumna();

            if (tablero2[x][y] == 0) {
                tablero2[x][y] = scoreBishop;
                i++;
            }
        }
        i = 0;

        while (i < knight) {
            x = generaFilaColumna();
            y = generaFilaColumna();

            if (tablero2[x][y] == 0) {
                tablero2[x][y] = scoreKnight;
                i++;
            }
        }
        i = 0;

        while (i < pawn) {
            x = generaFilaColumna();
            y = generaFilaColumna();

            if (tablero2[x][y] == 0) {
                tablero2[x][y] = scorePawn;
                i++;
            }
        }
        i = 0;
    }

//♔	♕	♖	♗	♘	♙	♚	♛	♜	♝	♞	♟
    public void imprimeCaracteres(int[][] tablero, Label[][] ltablero, boolean piezas) {
        for (int i = 0; i < tablero1.length; i++) {
            for (int j = 0; j < tablero1[0].length; j++) {
                if (piezas) {
                    if (tablero[i][j] == scoreKing) {
                        ltablero[i][j].setText("K");
                    }
                    if (tablero[i][j] == scoreQueen) {
                        ltablero[i][j].setText("Q");
                    }
                    if (tablero[i][j] == scoreRook) {
                        ltablero[i][j].setText("R");
                    }
                    if (tablero[i][j] == scoreBishop) {
                        ltablero[i][j].setText("B");
                    }
                    if (tablero[i][j] == scoreKnight) {
                        ltablero[i][j].setText("N");
                    }
                    if (tablero[i][j] == scorePawn) {
                        ltablero[i][j].setText("p");
                    }
                    if (tablero[i][j] == 0) {
                        ltablero[i][j].setText("");
                    }
                }

                int val = tablero[i][j];

                if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                    ltablero[i][j].setText("X");
                    ltablero[i][j].setBackground(Color.RED);
                    ltablero[i][j].setForeground(Color.WHITE);
                }

                if (val == 7) {
                    ltablero[i][j].setBackground(Color.GRAY);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bSalir) {
            Game menus = new Game();
            this.setVisible(false);
        }

        if (e.getSource() == bLanza) {

            if (score1 == 138) {
                JOptionPane.showMessageDialog(null, "¡Victoria! Presione el botón de salir", "", -1);
                return;
            }

            if (score2 == 138) {
                JOptionPane.showMessageDialog(null, "¡Derrota! Presione el botón de salir", "", -1);
                return;
            }

            try {
                int insFila = Integer.parseInt(tFila.getText());
                int insColumna = Integer.parseInt(tColumna.getText());

                if (insFila > 0 && insFila < 9) {
                    if (insColumna > 0 && insColumna < 9) {

                        bLanza.setBackground(Color.WHITE);
                        bLanza.setForeground(Color.BLACK);
                        bLanza.setLabel("Cargando");
                        bLanza.setFont(new Font("monospaced", Font.BOLD, 15));
                        bLanza.setEnabled(false);

                        score1 = revisar(tablero2, lTablero2, insColumna - 1, insFila - 1, score1, true);
                        tPuntaje.setText(Integer.toString(score1));

                        if (score1 == 138) {
                            JOptionPane.showMessageDialog(null, "¡Victoria! Presione el botón de salir", "", -1);
                            bLanza.setLabel("Final");
                            bLanza.setEnabled(true);
                            return;
                        }

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ie) {
                        }
                        juegaIA(tablero1, lTablero1);

                        if (score2 == 138) {
                            JOptionPane.showMessageDialog(null, "¡Derrota!", "", -1);
                            this.imprimeCaracteres(tablero2, lTablero2, true);
                            bLanza.setLabel("Final");
                            bLanza.setEnabled(true);
                            return;
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ie) {
                        }
                        bLanza.setLabel("¡Ataca!");
                        bLanza.setFont(new Font("monospaced", Font.BOLD, 17));
                        bLanza.setForeground(Color.WHITE);
                        bLanza.setEnabled(true);

                        if (modoPantalla.getSelectedIndex() == 0) {
                            bLanza.setBackground(Color.RED);
                        } else {
                            if (modoPantalla.getSelectedIndex() == 1) {
                                bLanza.setBackground(Color.BLUE);
                            } else {
                                bLanza.setBackground(Color.RED);
                            }
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Ingrese una columna válida.", "", 0);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese una fila válida.", " ", 0);
                }

            } catch (NumberFormatException q) {
                JOptionPane.showMessageDialog(null, "Ingresa un carácter válido.", "", 0);
            }
        }

        if (e.getSource() == modoPantalla) {
            if (modoPantalla.getSelectedIndex() == 0) {
                setBackground(Color.BLACK);
                lPuntaje.setBackground(Color.GREEN);
                lAux1.setBackground(Color.getHSBColor(60, 102, 255));
                bLanza.setBackground(Color.RED);

                for (int i = 0; i < tablero1.length; i++) {
                    for (int j = 0; j < tablero1[0].length; j++) {
                        if ((i + 1) % 2 == 0) {
                            if ((j + 1) % 2 == 0) {
                                lTablero1[i][j].setBackground(Color.WHITE);
                                lTablero2[i][j].setBackground(Color.WHITE);

                                int val1 = tablero1[i][j];

                                if (val1 == 1001 || val1 == 101 || val1 == 51 || val1 == 31 || val1 == 21 || val1 == 11) {
                                    lTablero1[i][j].setText("X");
                                    lTablero1[i][j].setBackground(Color.RED);
                                    lTablero1[i][j].setForeground(Color.WHITE);
                                }

                                if (val1 == 7) {
                                    lTablero1[i][j].setBackground(Color.GRAY);
                                }

                                int val = tablero2[i][j];

                                if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                                    lTablero2[i][j].setText("X");
                                    lTablero2[i][j].setBackground(Color.RED);
                                    lTablero2[i][j].setForeground(Color.WHITE);
                                }

                                if (val == 7) {
                                    lTablero2[i][j].setBackground(Color.GRAY);
                                }
                            } else {
                                lTablero1[i][j].setBackground(Color.BLACK);
                                lTablero1[i][j].setForeground(Color.YELLOW);
                                lTablero2[i][j].setBackground(Color.BLACK);
                                lTablero2[i][j].setForeground(Color.YELLOW);

                                int val1 = tablero1[i][j];

                                if (val1 == 1001 || val1 == 101 || val1 == 51 || val1 == 31 || val1 == 21 || val1 == 11) {
                                    lTablero1[i][j].setText("X");
                                    lTablero1[i][j].setBackground(Color.RED);
                                    lTablero1[i][j].setForeground(Color.WHITE);
                                }

                                if (val1 == 7) {
                                    lTablero1[i][j].setBackground(Color.GRAY);
                                }

                                int val = tablero2[i][j];

                                if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                                    lTablero2[i][j].setText("X");
                                    lTablero2[i][j].setBackground(Color.RED);
                                    lTablero2[i][j].setForeground(Color.WHITE);
                                }

                                if (val == 7) {
                                    lTablero2[i][j].setBackground(Color.GRAY);
                                }
                            }
                        } else {
                            if ((j + 1) % 2 == 0) {
                                lTablero1[i][j].setBackground(Color.BLACK);
                                lTablero1[i][j].setForeground(Color.YELLOW);
                                lTablero2[i][j].setBackground(Color.BLACK);
                                lTablero2[i][j].setForeground(Color.YELLOW);

                                int val1 = tablero1[i][j];

                                if (val1 == 1001 || val1 == 101 || val1 == 51 || val1 == 31 || val1 == 21 || val1 == 11) {
                                    lTablero1[i][j].setText("X");
                                    lTablero1[i][j].setBackground(Color.RED);
                                    lTablero1[i][j].setForeground(Color.WHITE);
                                }

                                if (val1 == 7) {
                                    lTablero1[i][j].setBackground(Color.GRAY);
                                }

                                int val = tablero2[i][j];

                                if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                                    lTablero2[i][j].setText("X");
                                    lTablero2[i][j].setBackground(Color.RED);
                                    lTablero2[i][j].setForeground(Color.WHITE);
                                }

                                if (val == 7) {
                                    lTablero2[i][j].setBackground(Color.GRAY);
                                }
                            } else {
                                lTablero1[i][j].setBackground(Color.WHITE);
                                lTablero2[i][j].setBackground(Color.WHITE);

                                int val1 = tablero1[i][j];

                                if (val1 == 1001 || val1 == 101 || val1 == 51 || val1 == 31 || val1 == 21 || val1 == 11) {
                                    lTablero1[i][j].setText("X");
                                    lTablero1[i][j].setBackground(Color.RED);
                                    lTablero1[i][j].setForeground(Color.WHITE);
                                }

                                if (val1 == 7) {
                                    lTablero1[i][j].setBackground(Color.GRAY);
                                }

                                int val = tablero2[i][j];

                                if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                                    lTablero2[i][j].setText("X");
                                    lTablero2[i][j].setBackground(Color.RED);
                                    lTablero2[i][j].setForeground(Color.WHITE);
                                }

                                if (val == 7) {
                                    lTablero2[i][j].setBackground(Color.GRAY);
                                }

                            }
                        }
                    }
                }

            } else {
                setBackground(Color.getHSBColor(60, 102, 255));
                lPuntaje.setBackground(Color.GREEN);
                lAux1.setBackground(Color.BLACK);
                bLanza.setBackground(Color.BLUE);

                for (int i = 0; i < tablero1.length; i++) {
                    for (int j = 0; j < tablero1[0].length; j++) {
                        if ((i + 1) % 2 == 0) {
                            if ((j + 1) % 2 == 0) {
                                lTablero1[i][j].setBackground(Color.WHITE);
                                lTablero2[i][j].setBackground(Color.WHITE);

                                int val1 = tablero1[i][j];

                                if (val1 == 1001 || val1 == 101 || val1 == 51 || val1 == 31 || val1 == 21 || val1 == 11) {
                                    lTablero1[i][j].setText("X");
                                    lTablero1[i][j].setBackground(Color.RED);
                                    lTablero1[i][j].setForeground(Color.WHITE);
                                }

                                if (val1 == 7) {
                                    lTablero1[i][j].setBackground(Color.GRAY);
                                }

                                int val = tablero2[i][j];

                                if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                                    lTablero2[i][j].setText("X");
                                    lTablero2[i][j].setBackground(Color.RED);
                                    lTablero2[i][j].setForeground(Color.WHITE);
                                }

                                if (val == 7) {
                                    lTablero2[i][j].setBackground(Color.GRAY);
                                }

                            } else {
                                lTablero1[i][j].setBackground(Color.GREEN);
                                lTablero1[i][j].setForeground(Color.DARK_GRAY);
                                lTablero2[i][j].setBackground(Color.GREEN);
                                lTablero2[i][j].setForeground(Color.DARK_GRAY);

                                int val1 = tablero1[i][j];

                                if (val1 == 1001 || val1 == 101 || val1 == 51 || val1 == 31 || val1 == 21 || val1 == 11) {
                                    lTablero1[i][j].setText("X");
                                    lTablero1[i][j].setBackground(Color.RED);
                                    lTablero1[i][j].setForeground(Color.WHITE);
                                }

                                if (val1 == 7) {
                                    lTablero1[i][j].setBackground(Color.GRAY);
                                }

                                int val = tablero2[i][j];

                                if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                                    lTablero2[i][j].setText("X");
                                    lTablero2[i][j].setBackground(Color.RED);
                                    lTablero2[i][j].setForeground(Color.WHITE);
                                }

                                if (val == 7) {
                                    lTablero2[i][j].setBackground(Color.GRAY);
                                }
                            }
                        } else {
                            if ((j + 1) % 2 == 0) {
                                lTablero1[i][j].setBackground(Color.GREEN);
                                lTablero1[i][j].setForeground(Color.DARK_GRAY);
                                lTablero2[i][j].setBackground(Color.GREEN);
                                lTablero2[i][j].setForeground(Color.DARK_GRAY);

                                int val1 = tablero1[i][j];

                                if (val1 == 1001 || val1 == 101 || val1 == 51 || val1 == 31 || val1 == 21 || val1 == 11) {
                                    lTablero1[i][j].setText("X");
                                    lTablero1[i][j].setBackground(Color.RED);
                                    lTablero1[i][j].setForeground(Color.WHITE);
                                }

                                if (val1 == 7) {
                                    lTablero1[i][j].setBackground(Color.GRAY);
                                }

                                int val = tablero2[i][j];

                                if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                                    lTablero2[i][j].setText("X");
                                    lTablero2[i][j].setBackground(Color.RED);
                                    lTablero2[i][j].setForeground(Color.WHITE);
                                }

                                if (val == 7) {
                                    lTablero2[i][j].setBackground(Color.GRAY);
                                }

                            } else {
                                lTablero1[i][j].setBackground(Color.WHITE);
                                lTablero2[i][j].setBackground(Color.WHITE);

                                int val1 = tablero1[i][j];

                                if (val1 == 1001 || val1 == 101 || val1 == 51 || val1 == 31 || val1 == 21 || val1 == 11) {
                                    lTablero1[i][j].setText("X");
                                    lTablero1[i][j].setBackground(Color.RED);
                                    lTablero1[i][j].setForeground(Color.WHITE);
                                }

                                if (val1 == 7) {
                                    lTablero1[i][j].setBackground(Color.GRAY);
                                }

                                int val = tablero2[i][j];

                                if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                                    lTablero2[i][j].setText("X");
                                    lTablero2[i][j].setBackground(Color.RED);
                                    lTablero2[i][j].setForeground(Color.WHITE);
                                }

                                if (val == 7) {
                                    lTablero2[i][j].setBackground(Color.GRAY);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (e.getSource() == bCarga) {
            if (f.exists()) {
                score1 = 0;
                score2 = 0;
                reseteaTodo();
                cargaPosicion();
                tPuntaje.setText(Integer.toString(score1));
                this.imprimeCaracteres(tablero1, lTablero1, true);
                this.imprimeCaracteres(tablero2, lTablero2, false);

                if (modoPantalla.getSelectedIndex() == 0) {
                    setBackground(Color.BLACK);
                    lPuntaje.setBackground(Color.GREEN);
                    lAux1.setBackground(Color.getHSBColor(60, 102, 255));
                    bLanza.setBackground(Color.RED);

                    for (int i = 0; i < tablero1.length; i++) {
                        for (int j = 0; j < tablero1[0].length; j++) {
                            if ((i + 1) % 2 == 0) {
                                if ((j + 1) % 2 == 0) {
                                    lTablero1[i][j].setBackground(Color.WHITE);
                                    lTablero2[i][j].setBackground(Color.WHITE);

                                    int val1 = tablero1[i][j];

                                    if (val1 == 1001 || val1 == 101 || val1 == 51 || val1 == 31 || val1 == 21 || val1 == 11) {
                                        lTablero1[i][j].setText("X");
                                        lTablero1[i][j].setBackground(Color.RED);
                                        lTablero1[i][j].setForeground(Color.WHITE);
                                    }

                                    if (val1 == 7) {
                                        lTablero1[i][j].setBackground(Color.GRAY);
                                    }

                                    int val = tablero2[i][j];

                                    if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                                        lTablero2[i][j].setText("X");
                                        lTablero2[i][j].setBackground(Color.RED);
                                        lTablero2[i][j].setForeground(Color.WHITE);
                                    }

                                    if (val == 7) {
                                        lTablero2[i][j].setBackground(Color.GRAY);
                                    }
                                } else {
                                    lTablero1[i][j].setBackground(Color.BLACK);
                                    lTablero1[i][j].setForeground(Color.YELLOW);
                                    lTablero2[i][j].setBackground(Color.BLACK);
                                    lTablero2[i][j].setForeground(Color.YELLOW);

                                    int val1 = tablero1[i][j];

                                    if (val1 == 1001 || val1 == 101 || val1 == 51 || val1 == 31 || val1 == 21 || val1 == 11) {
                                        lTablero1[i][j].setText("X");
                                        lTablero1[i][j].setBackground(Color.RED);
                                        lTablero1[i][j].setForeground(Color.WHITE);
                                    }

                                    if (val1 == 7) {
                                        lTablero1[i][j].setBackground(Color.GRAY);
                                    }

                                    int val = tablero2[i][j];

                                    if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                                        lTablero2[i][j].setText("X");
                                        lTablero2[i][j].setBackground(Color.RED);
                                        lTablero2[i][j].setForeground(Color.WHITE);
                                    }

                                    if (val == 7) {
                                        lTablero2[i][j].setBackground(Color.GRAY);
                                    }
                                }
                            } else {
                                if ((j + 1) % 2 == 0) {
                                    lTablero1[i][j].setBackground(Color.BLACK);
                                    lTablero1[i][j].setForeground(Color.YELLOW);
                                    lTablero2[i][j].setBackground(Color.BLACK);
                                    lTablero2[i][j].setForeground(Color.YELLOW);

                                    int val1 = tablero1[i][j];

                                    if (val1 == 1001 || val1 == 101 || val1 == 51 || val1 == 31 || val1 == 21 || val1 == 11) {
                                        lTablero1[i][j].setText("X");
                                        lTablero1[i][j].setBackground(Color.RED);
                                        lTablero1[i][j].setForeground(Color.WHITE);
                                    }

                                    if (val1 == 7) {
                                        lTablero1[i][j].setBackground(Color.GRAY);
                                    }

                                    int val = tablero2[i][j];

                                    if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                                        lTablero2[i][j].setText("X");
                                        lTablero2[i][j].setBackground(Color.RED);
                                        lTablero2[i][j].setForeground(Color.WHITE);
                                    }

                                    if (val == 7) {
                                        lTablero2[i][j].setBackground(Color.GRAY);
                                    }
                                } else {
                                    lTablero1[i][j].setBackground(Color.WHITE);
                                    lTablero2[i][j].setBackground(Color.WHITE);

                                    int val1 = tablero1[i][j];

                                    if (val1 == 1001 || val1 == 101 || val1 == 51 || val1 == 31 || val1 == 21 || val1 == 11) {
                                        lTablero1[i][j].setText("X");
                                        lTablero1[i][j].setBackground(Color.RED);
                                        lTablero1[i][j].setForeground(Color.WHITE);
                                    }

                                    if (val1 == 7) {
                                        lTablero1[i][j].setBackground(Color.GRAY);
                                    }

                                    int val = tablero2[i][j];

                                    if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                                        lTablero2[i][j].setText("X");
                                        lTablero2[i][j].setBackground(Color.RED);
                                        lTablero2[i][j].setForeground(Color.WHITE);
                                    }

                                    if (val == 7) {
                                        lTablero2[i][j].setBackground(Color.GRAY);
                                    }

                                }
                            }
                        }
                    }

                } else {
                    setBackground(Color.getHSBColor(60, 102, 255));
                    lPuntaje.setBackground(Color.GREEN);
                    lAux1.setBackground(Color.BLACK);
                    bLanza.setBackground(Color.BLUE);

                    for (int i = 0; i < tablero1.length; i++) {
                        for (int j = 0; j < tablero1[0].length; j++) {
                            if ((i + 1) % 2 == 0) {
                                if ((j + 1) % 2 == 0) {
                                    lTablero1[i][j].setBackground(Color.WHITE);
                                    lTablero2[i][j].setBackground(Color.WHITE);

                                    int val1 = tablero1[i][j];

                                    if (val1 == 1001 || val1 == 101 || val1 == 51 || val1 == 31 || val1 == 21 || val1 == 11) {
                                        lTablero1[i][j].setText("X");
                                        lTablero1[i][j].setBackground(Color.RED);
                                        lTablero1[i][j].setForeground(Color.WHITE);
                                    }

                                    if (val1 == 7) {
                                        lTablero1[i][j].setBackground(Color.GRAY);
                                    }

                                    int val = tablero2[i][j];

                                    if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                                        lTablero2[i][j].setText("X");
                                        lTablero2[i][j].setBackground(Color.RED);
                                        lTablero2[i][j].setForeground(Color.WHITE);
                                    }

                                    if (val == 7) {
                                        lTablero2[i][j].setBackground(Color.GRAY);
                                    }

                                } else {
                                    lTablero1[i][j].setBackground(Color.GREEN);
                                    lTablero1[i][j].setForeground(Color.DARK_GRAY);
                                    lTablero2[i][j].setBackground(Color.GREEN);
                                    lTablero2[i][j].setForeground(Color.DARK_GRAY);

                                    int val1 = tablero1[i][j];

                                    if (val1 == 1001 || val1 == 101 || val1 == 51 || val1 == 31 || val1 == 21 || val1 == 11) {
                                        lTablero1[i][j].setText("X");
                                        lTablero1[i][j].setBackground(Color.RED);
                                        lTablero1[i][j].setForeground(Color.WHITE);
                                    }

                                    if (val1 == 7) {
                                        lTablero1[i][j].setBackground(Color.GRAY);
                                    }

                                    int val = tablero2[i][j];

                                    if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                                        lTablero2[i][j].setText("X");
                                        lTablero2[i][j].setBackground(Color.RED);
                                        lTablero2[i][j].setForeground(Color.WHITE);
                                    }

                                    if (val == 7) {
                                        lTablero2[i][j].setBackground(Color.GRAY);
                                    }
                                }
                            } else {
                                if ((j + 1) % 2 == 0) {
                                    lTablero1[i][j].setBackground(Color.GREEN);
                                    lTablero1[i][j].setForeground(Color.DARK_GRAY);
                                    lTablero2[i][j].setBackground(Color.GREEN);
                                    lTablero2[i][j].setForeground(Color.DARK_GRAY);

                                    int val1 = tablero1[i][j];

                                    if (val1 == 1001 || val1 == 101 || val1 == 51 || val1 == 31 || val1 == 21 || val1 == 11) {
                                        lTablero1[i][j].setText("X");
                                        lTablero1[i][j].setBackground(Color.RED);
                                        lTablero1[i][j].setForeground(Color.WHITE);
                                    }

                                    if (val1 == 7) {
                                        lTablero1[i][j].setBackground(Color.GRAY);
                                    }

                                    int val = tablero2[i][j];

                                    if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                                        lTablero2[i][j].setText("X");
                                        lTablero2[i][j].setBackground(Color.RED);
                                        lTablero2[i][j].setForeground(Color.WHITE);
                                    }

                                    if (val == 7) {
                                        lTablero2[i][j].setBackground(Color.GRAY);
                                    }

                                } else {
                                    lTablero1[i][j].setBackground(Color.WHITE);
                                    lTablero2[i][j].setBackground(Color.WHITE);

                                    int val1 = tablero1[i][j];

                                    if (val1 == 1001 || val1 == 101 || val1 == 51 || val1 == 31 || val1 == 21 || val1 == 11) {
                                        lTablero1[i][j].setText("X");
                                        lTablero1[i][j].setBackground(Color.RED);
                                        lTablero1[i][j].setForeground(Color.WHITE);
                                    }

                                    if (val1 == 7) {
                                        lTablero1[i][j].setBackground(Color.GRAY);
                                    }

                                    int val = tablero2[i][j];

                                    if (val == 1001 || val == 101 || val == 51 || val == 31 || val == 21 || val == 11) {
                                        lTablero2[i][j].setText("X");
                                        lTablero2[i][j].setBackground(Color.RED);
                                        lTablero2[i][j].setForeground(Color.WHITE);
                                    }

                                    if (val == 7) {
                                        lTablero2[i][j].setBackground(Color.GRAY);
                                    }
                                }
                            }
                        }
                    }
                }

                JOptionPane.showMessageDialog(null, "Partida cargada.", "", -1);
            } else {
                JOptionPane.showMessageDialog(null, "No hay partidas guardadas.", "", 1);
            }
        }

        if (e.getSource() == bGuarda) {
            f.delete();
            guardaPosicion();
            JOptionPane.showMessageDialog(null, "Partida guardada.", "", -1);
        }

        if (e.getSource() == bReset) {

            if (score1 == 138) {
                JOptionPane.showMessageDialog(null, "¡Victoria! Presione el botón de salir", "", -1);
                return;
            }

            if (score2 == 138) {
                JOptionPane.showMessageDialog(null, "¡Derrota! Presione el botón de salir", "", -1);
                return;
            }
            JOptionPane.showMessageDialog(null, "¡Te has rendido! Saliendo de la partida.", "", 0);
            Game menus = new Game();
            this.setVisible(false);
        }
    }

    public void reseteaTodo() {

        for (int i = 0; i < tablero1.length; i++) {
            for (int j = 0; j < tablero1[0].length; j++) {
                tablero1[i][j] = 0;
                tablero2[i][j] = 0;

                if ((i + 1) % 2 == 0) {
                    if ((j + 1) % 2 == 0) {
                        lTablero1[i][j].setBackground(Color.WHITE);
                        lTablero2[i][j].setBackground(Color.WHITE);
                    } else {
                        lTablero1[i][j].setBackground(Color.BLACK);
                        lTablero1[i][j].setForeground(Color.YELLOW);

                        lTablero2[i][j].setBackground(Color.BLACK);
                        lTablero2[i][j].setForeground(Color.YELLOW);
                    }
                } else {
                    if ((j + 1) % 2 == 0) {
                        lTablero1[i][j].setBackground(Color.BLACK);
                        lTablero1[i][j].setForeground(Color.YELLOW);

                        lTablero2[i][j].setBackground(Color.BLACK);
                        lTablero2[i][j].setForeground(Color.YELLOW);
                    } else {
                        lTablero1[i][j].setBackground(Color.WHITE);
                        lTablero2[i][j].setBackground(Color.WHITE);
                    }
                }

            }
        }
        this.imprimeCaracteres(tablero1, lTablero1, true);
        this.imprimeCaracteres(tablero2, lTablero2, true);
    }

    public int revisar(int[][] tablero, Label[][] lTablero, int col, int fil, int scoreTurno, boolean jugador) {

        if ((tablero[col][fil] == 1001 || tablero[col][fil] == 101 || tablero[col][fil] == 51 || tablero[col][fil] == 31 || tablero[col][fil] == 21 || tablero[col][fil] == 11 || tablero[col][fil] == 7) && jugador) {
            JOptionPane.showMessageDialog(null, "Tiro desperdiciado. Ya atacaste esta casilla.", "", 0);
            return scoreTurno;
        }

        switch (tablero[col][fil]) {
            case 100:
                tablero[col][fil] = 1001;
                lTablero[col][fil].setText("X");
                lTablero[col][fil].setBackground(Color.RED);
                lTablero[col][fil].setForeground(Color.WHITE);
                scoreTurno += scoreKing;
                break;
            case 10:
                tablero[col][fil] = 101;
                lTablero[col][fil].setText("X");
                lTablero[col][fil].setBackground(Color.RED);
                lTablero[col][fil].setForeground(Color.WHITE);
                scoreTurno += scoreQueen;
                break;
            case 5:
                tablero[col][fil] = 51;
                lTablero[col][fil].setText("X");
                lTablero[col][fil].setBackground(Color.RED);
                lTablero[col][fil].setForeground(Color.WHITE);
                scoreTurno += scoreRook;
                break;
            case 4:
                scoreTurno += 0;
                break;
            case 3:
                tablero[col][fil] = 31;
                lTablero[col][fil].setText("X");
                lTablero[col][fil].setBackground(Color.RED);
                lTablero[col][fil].setForeground(Color.WHITE);
                scoreTurno += scoreBishop;
                break;
            case 2:
                tablero[col][fil] = 21;
                lTablero[col][fil].setText("X");
                lTablero[col][fil].setBackground(Color.RED);
                lTablero[col][fil].setForeground(Color.WHITE);
                scoreTurno += scoreKnight;
                break;
            case 1:
                tablero[col][fil] = 11;
                lTablero[col][fil].setText("X");
                lTablero[col][fil].setBackground(Color.RED);
                lTablero[col][fil].setForeground(Color.WHITE);
                scoreTurno += scorePawn;
                break;
            case 0:
                tablero[col][fil] = 7;
                lTablero[col][fil].setText("");
                lTablero[col][fil].setBackground(Color.GRAY);
                scoreTurno += 0;
                break;
        }
        return scoreTurno;
    }

    public void juegaIA(int[][] tablero, Label[][] lTablero) {
        int x = generaFilaColumna();
        int y = generaFilaColumna();

        if (tablero[y][x] == 1001 || tablero[y][x] == 101 || tablero[y][x] == 51 || tablero[y][x] == 31 || tablero[y][x] == 21 || tablero[y][x] == 11 || tablero[y][x] == 7) {
            juegaIA(tablero, lTablero);

        } else {
            score2 = revisar(tablero, lTablero, y, x, score2, false);
        }
    }

    public void guardaPosicion() {
        String lineaTab1 = Game.userName + "|" + score1;

        for (int i = 0; i < tablero1.length; i++) {
            for (int j = 0; j < tablero1[0].length; j++) {
                lineaTab1 += "|" + tablero1[i][j];
            }
        }
        grabaArchivo(lineaTab1);

        String lineaTab2 = "EsteNombreEsMuyLargoYNadieLoVaAPoderIgualarNuncaEnEstePrograma|" + score2;
        for (int i = 0; i < tablero1.length; i++) {
            for (int j = 0; j < tablero1[0].length; j++) {
                lineaTab2 += "|" + tablero2[i][j];
            }
        }
        grabaArchivo(lineaTab2);
    }

    public void creaArchivo(File arch) {
        try {
            arch.createNewFile();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "El archivo ya existe.", "Error", 0);
        }
    }

    public void cargaPosicion() {
        String aux;
        int linea = 0;
        try {
            if (!f.exists()) {
                JOptionPane.showMessageDialog(null, "No hay partidas guardadas.", "", 1);
                return;
            }
            BufferedReader br = new BufferedReader(new FileReader(f));
            while (true) {
                aux = br.readLine();
                if (aux == null) {
                    break;
                }
                String lineaAReemplazar = aux;
                String datos[] = aux.split("\\|");
                int sumador = 2;

                if (linea == 0) {
                    this.score1 = Integer.parseInt(datos[1]);
                    for (int i = 0; i < tablero1.length; i++) {
                        for (int j = 0; j < tablero1[0].length; j++) {
                            tablero1[i][j] = Integer.parseInt(datos[sumador]);
                            sumador++;
                        }
                    }
                    linea++;
                    sumador = 2;
                }

                if (linea == 1) {
                    this.score2 = Integer.parseInt(datos[1]);
                    for (int i = 0; i < tablero2.length; i++) {
                        for (int j = 0; j < tablero2[0].length; j++) {
                            tablero2[i][j] = Integer.parseInt(datos[sumador]);
                            sumador++;
                        }
                    }
                }
            }
            br.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error en el archivo: " + e, "Error", 0);
        }
    }

    public void grabaArchivo(String linea) {
        try {
            //f.delete();
            if (!f.exists()) {
                creaArchivo(f);
            }
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, true), "utf-8"));
            bw.write(linea + "\r\n");
            bw.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error en el archivo: " + e, "Error", 0);
        }
    }

    @Override
    public void run() {
        try {
            for (int seg = 0; !partidaAcabada; seg++) {
                Thread.sleep(1000);
                if (seg < 10) {
                    lTiempo.setText("0:0" + seg);
                } else {
                    if (seg < 60) {
                        lTiempo.setText("0:" + seg);
                    } else {
                        if (seg >= 60) {

                            int min = seg / 60;
                            int nseg = seg % 60;

                            if (nseg < 10) {
                                lTiempo.setText(min + ":0" + nseg);
                            } else {
                                lTiempo.setText(min + ":" + nseg);
                            }
                        }
                    }
                }
            }
        } catch (InterruptedException e) {

        }
    }
}
