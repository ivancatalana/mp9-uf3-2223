## UDP

>**Tasca 1**  
>Implementeu el codi d'exemple que hi ha als apunts (Servidor i CLient), i hi afegiu el següent:  
>Feu que:
> - El client hagi d'entrar el seu nom en la primera connexió amb el servidor.
> - Que cada cosa que el client li envia al servidor, el servidor ho tregui per consola.
> - Si el client li envia un "adeu" al servidor, el client es desconnecta, però no pas el servidor.
> - Que el servidor envii com a resposta al client el mateix que el client li ha enviat però en majúscules

*Per tal de discrimar el bytes restants del packet i poder obtenir el missatge correcte, feu servir el següent*
```java
    String msg = new String(data,0,lenght);
    //data és l'array de bytes rebuts
```
<hr>

