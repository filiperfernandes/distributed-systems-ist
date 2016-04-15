# Projeto de Sistemas Distribuídos 2015-2016 #

Grupo de SD 30 - Campus Taguspark

André Mendes 78079 andre.m.mendes@tecnico.ulisboa.pt

Filipe Fernandes 78083 filipe.r.fernandes@tecnico.ulisboa.pt

João Bernardino 78022 joao.bernardino@tecnico.ulisboa.pt


Repositório:
[tecnico-softeng-distsys-2015/T_30-project](https://github.com/tecnico-softeng-distsys-2015/T_30-project/)

-------------------------------------------------------------------------------

## Instruções de instalação


### Ambiente

[0] Iniciar sistema operativo

Indicar Windows ou Linux
*(escolher um dos dois, que esteja disponível nos laboratórios, e depois apagar esta linha)*


[1] Iniciar servidores de apoio

JUDDI:
cd /juddi-3.3.2_tomcat-7.0.64_9090/bin
If linux or macos
sh startup.sh
If windows
startup.bat


[2] Criar pasta temporária

cd ~/Desktop
mkdir Projecto-sdis-T30


[3] Obter código fonte do projeto (versão entregue)

```
git clone ...
```
git clone -b SD_R1 https://github.com/tecnico-softeng-distsys-2015/T_30-project.git


[4] Instalar módulos de bibliotecas auxiliares

```
cd uddi-naming
mvn clean install
```


-------------------------------------------------------------------------------

### Serviço TRANSPORTER

[1] Construir e executar **servidor**

```
cd Projecto-sdis-T30/T_30-project/transporter-ws
mvn clean install
mvn exec:java
```
[2] Construir e executar **servidor**

```
cd Projecto-sdis-T30/T_30-project/transporter-ws
mvn -Dws.i=2 exec:java
```


[3] Construir **cliente** e executar testes

```
cd Projecto-sdis-T30/T_30-project/transporter-ws-cli
mvn clean install
```

...


-------------------------------------------------------------------------------

### Serviço BROKER

[1] Construir e executar **servidor**

```
cd Projecto-sdis-T30/T_30-project/broker-ws
mvn clean install
mvn exec:java
```


[2] Construir **cliente** e executar testes

```
cd Projecto-sdis-T30/T_30-project/broker-ws-cli
mvn clean install
```

...

-------------------------------------------------------------------------------
**FIM**
