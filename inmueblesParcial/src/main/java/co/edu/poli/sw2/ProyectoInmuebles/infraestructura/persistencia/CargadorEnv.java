package co.edu.poli.sw2.ProyectoInmuebles.infraestructura.persistencia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Lee el archivo {@code .env} de la raiz del proyecto (una sola vez).
 * <p>
 * Formato: una variable por linea, {@code CLAVE=valor}. Las lineas vacias
 * y las que empiezan con {@code #} se ignoran.
 *
 * @author Equipo Gestion de Inmuebles
 */
final class CargadorEnv {

    /** Rutas donde se busca el .env, segun desde donde se ejecute. */
    private static final String[] RUTAS = { ".env", "ProyectoInmuebles/.env", "../.env" };

    /** Valores leidos del archivo. */
    private static final Map<String, String> VALORES = cargar();

    /**
     * Evita crear instancias.
     */
    private CargadorEnv() {
    }

    /**
     * Devuelve el valor de una clave del .env.
     *
     * @param clave nombre de la variable
     * @return el valor, o {@code null} si no existe
     */
    static String obtener(String clave) {
        return VALORES.get(clave);
    }

    /**
     * Busca y lee el primer .env que exista.
     *
     * @return mapa inmutable con las variables, vacio si no hay archivo
     */
    private static Map<String, String> cargar() {
        for (String ruta : RUTAS) {
            Path archivo = Paths.get(ruta);
            if (Files.isRegularFile(archivo)) {
                try {
                    Map<String, String> mapa = new HashMap<>();
                    for (String linea : Files.readAllLines(archivo, StandardCharsets.UTF_8)) {
                        linea = linea.replace("﻿", "").trim(); // quita BOM si existe
                        if (linea.isEmpty() || linea.startsWith("#")) {
                            continue;
                        }
                        int igual = linea.indexOf('=');
                        if (igual > 0) {
                            mapa.put(linea.substring(0, igual).trim(), linea.substring(igual + 1).trim());
                        }
                    }
                    return Collections.unmodifiableMap(mapa);
                } catch (IOException e) {
                    return Collections.emptyMap();
                }
            }
        }
        return Collections.emptyMap();
    }
}
