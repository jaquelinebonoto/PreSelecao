CREATE TABLE APP_ROLE(
  ID NUMBER PRIMARY KEY NOT NULL,
  ROLENAME VARCHAR2(50),
  DESCRIPTION VARCHAR2(50)
  );
  
  CREATE SEQUENCE S_ROLE;
  SELECT * FROM APP_ROLE;

CREATE TABLE APP_USER (
  ID NUMBER PRIMARY KEY NOT NULL,
  USERNAME VARCHAR2(50) NOT NULL,
  PASSWORD VARCHAR2(50) NOT NULL,
  EMAIL VARCHAR2(50) NOT NULL
  );
  
  CREATE SEQUENCE S_USER;
  SELECT * FROM APP_USER;
  
  
CREATE TABLE CANDIDATO (
  ID NUMBER PRIMARY KEY NOT NULL,
  NOME VARCHAR2(70) NOT NULL,
  DATA_NASCIMENTO DATE NOT NULL,
  CPF VARCHAR2(14) NOT NULL,
  ID_USER NOT NULL REFERENCES APP_USER (ID)
  );
  
  CREATE SEQUENCE S_CANDIDATO;
  SELECT * FROM CANDIDATO;
  
  CREATE TABLE APP_ADMIN (
    ID NUMBER PRIMARY KEY NOT NULL,
    ID_USER NOT NULL REFERENCES APP_USER (ID)
  );
  
  CREATE SEQUENCE S_ADMIN;
  SELECT * FROM APP_ADMIN;
  
  CREATE TABLE ENDERECO(
    LOGRADOURO VARCHAR2(20) NOT NULL,
    NUMERO NUMBER(20) NOT NULL,
    COMPLEMENTO VARCHAR2(20),
    CEP VARCHAR2(11) NOT NULL,
    BAIRRO VARCHAR2(20) NOT NULL,
    CIDADE VARCHAR2(20) NOT NULL,
    ESTADO VARCHAR2(20) NOT NULL,
    ID_CANDIDATO NOT NULL REFERENCES CANDIDATO(ID)
  );
  
  CREATE SEQUENCE S_ENDERECO;
  SELECT * FROM ENDERECO;
  
  
  
  
  
  DROP TABLE APP_ROLE;
  DROP TABLE APP_USER;
  DROP TABLE CANDIDATO;
  DROP TABLE APP_ADMIN;
  DROP TABLE ENDERECO;
  
  DROP SEQUENCE S_ROLE;
  DROP SEQUENCE S_USER;
  DROP SEQUENCE S_CANDIDATO;
  DROP SEQUENCE S_ADMIN;
  DROP SEQUENCE S_ENDERECO;
  
  COMMIT;

