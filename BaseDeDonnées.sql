<<<<<<< HEAD
/*CREATE DATABASE [Encheres];*/
DROP TABLE [ENCHERE];
DROP TABLE [RETRAIT];
DROP TABLE [ARTICLE];
DROP TABLE [ROLES];
DROP TABLE [UTILISATEUR];
DROP TABLE [CATEGORIE];

CREATE TABLE [UTILISATEUR](
	[idUtilisateur] int PRIMARY KEY IDENTITY,
	[pseudo] [NVARCHAR](150) UNIQUE NOT NULL,
	[nom] [NVARCHAR](150) NOT NULL,
	[prenom] [NVARCHAR](150) NOT NULL,
	[email] [NVARCHAR](250) NOT NULL,
	[telephone] [VARCHAR](10) NOT NULL,
	[rue] [NVARCHAR](200) NOT NULL,
	[codePostal] VARCHAR (5) NOT NULL,
	[ville] [NVARCHAR](150) NOT NULL,
	[motDePasse] [NVARCHAR](150) NOT NULL,
	[credit] int NOT NULL,
	[administrateur] bit NOT NULL,
	);

CREATE TABLE [ARTICLE](
	[idArticle] int PRIMARY KEY IDENTITY,
	[nomArticle] [NVARCHAR](150) NOT NULL,
	[description] [NVARCHAR](255) NOT NULL,
	[dateDebutEncheres] date NOT NULL,
	[dateFinEncheres] date NOT NULL,
	[miseAPrix] int NOT NULL,
	[prixVente] int NOT NULL,
	[etatVente] int NOT NULL,
	[idCategorie] int NOT NULL,
	[idUtilisateur] int NOT NULL,
	[imageArticle] [NVARCHAR](250),
	)

CREATE TABLE [ENCHERE](
	[idEnchere] int PRIMARY KEY IDENTITY,
	[dateEnchere] date NOT NULL,
	[montantEnchere] int NOT NULL,
	[idUtilisateur] int  NOT NULL,
	[idArticle] int NOT NULL,
	);

CREATE TABLE [CATEGORIE](
	[idCategorie] int PRIMARY KEY IDENTITY,
	[libelle] [NVARCHAR](300) NOT NULL,
	)

CREATE TABLE [RETRAIT](
	[idArticle] int ,
	[rue] [NVARCHAR](200) NOT NULL,
	[codePostal] VARCHAR (5) NOT NULL,
	[ville] [NVARCHAR](150) NOT NULL,
	)
 
CREATE TABLE [ROLES](
	[pseudo] [NVARCHAR](150) NOT NULL,
	[role] [VARCHAR](50) NOT NULL,
	)
 
ALTER TABLE ROLES add
	CONSTRAINT PK_ROLES PRIMARY KEY (pseudo,role);
 
ALTER TABLE ARTICLE add
	CONSTRAINT FK_ARTICLE_UTILISATEUR FOREIGN KEY (idUtilisateur)
	REFERENCES UTILISATEUR(idUtilisateur);
ALTER TABLE ARTICLE add
	CONSTRAINT FK_ARTICLE_CATEGORIE FOREIGN KEY (idCategorie)
	REFERENCES CATEGORIE (idCategorie);
ALTER TABLE RETRAIT add
	CONSTRAINT FK_RETRAIT_ARTICLE FOREIGN KEY (idArticle)
	REFERENCES ARTICLE (idArticle);
ALTER TABLE ENCHERE add
	CONSTRAINT FK_ENCHERE_UTILISATEUR FOREIGN KEY (idUtilisateur)
	REFERENCES UTILISATEUR (idUtilisateur);
ALTER TABLE ENCHERE add
	CONSTRAINT FK_ENCHERE_ARTICLE FOREIGN KEY (idArticle)
	REFERENCES ARTICLE (idArticle);
 
ALTER TABLE ROLES add
	constraint FK_ROLES_UTILISATEURS foreign key (pseudo)
	references UTILISATEUR(pseudo);
=======
/*CREATE DATABASE [Encheres];*/
 
DROP TABLE [ENCHERE];
DROP TABLE [RETRAIT];
DROP TABLE [ARTICLE];
DROP TABLE [ROLES];
DROP TABLE [UTILISATEUR];
DROP TABLE [CATEGORIE];
 
 
 
CREATE TABLE [UTILISATEUR](
	[idUtilisateur] int PRIMARY KEY IDENTITY,
	[pseudo] [NVARCHAR](150) UNIQUE NOT NULL,
	[nom] [NVARCHAR](150) NOT NULL,
	[prenom] [NVARCHAR](150) NOT NULL,
	[email] [NVARCHAR](250) NOT NULL,
	[telephone] [VARCHAR](10) NOT NULL,
	[rue] [NVARCHAR](200) NOT NULL,
	[codePostal] VARCHAR (5) NOT NULL,
	[ville] [NVARCHAR](150) NOT NULL,
	[motDePasse] [NVARCHAR](150) NOT NULL,
	[credit] int NOT NULL,
	[administrateur] bit NOT NULL,
	);
 
	
CREATE TABLE [ARTICLE](
	[idArticle] int PRIMARY KEY IDENTITY,
	[nomArticle] [NVARCHAR](150) NOT NULL,
	[description] [NVARCHAR](255) NOT NULL,
	[dateDebutEncheres] date NOT NULL,
	[dateFinEncheres] date NOT NULL,
	[miseAPrix] int NOT NULL,
	[prixVente] int NOT NULL,
	[etatVente] int NOT NULL,
	[idCategorie] int NOT NULL,
	[idUtilisateur] int NOT NULL,
	[imageArticle] [VARCHAR](250),
	)
 
 
CREATE TABLE [ENCHERE](
	[idEnchere] int PRIMARY KEY IDENTITY,
	[dateEnchere] date NOT NULL,
	[montantEnchere] int NOT NULL,
	[idUtilisateur] int  NOT NULL,
	[idArticle] int NOT NULL,
	);
 
 
CREATE TABLE [CATEGORIE](
	[idCategorie] int PRIMARY KEY IDENTITY,
	[libelle] [NVARCHAR](300) NOT NULL,
	)
 
 
CREATE TABLE [RETRAIT](
	[idArticle] int ,
	[rue] [NVARCHAR](200) NOT NULL,
	[codePostal] VARCHAR (5) NOT NULL,
	[ville] [NVARCHAR](150) NOT NULL,
	)

CREATE TABLE [ROLES](
	[pseudo] [NVARCHAR](150) NOT NULL,
	[role] [VARCHAR](50) NOT NULL,
	)

ALTER TABLE ROLES add
	CONSTRAINT PK_ROLES PRIMARY KEY (pseudo,role);

ALTER TABLE ARTICLE add
	CONSTRAINT FK_ARTICLE_UTILISATEUR FOREIGN KEY (idUtilisateur)
	REFERENCES UTILISATEUR(idUtilisateur);
 
ALTER TABLE ARTICLE add
	CONSTRAINT FK_ARTICLE_CATEGORIE FOREIGN KEY (idCategorie)
	REFERENCES CATEGORIE (idCategorie);
 
ALTER TABLE RETRAIT add
	CONSTRAINT FK_RETRAIT_ARTICLE FOREIGN KEY (idArticle)
	REFERENCES ARTICLE (idArticle);
 
ALTER TABLE ENCHERE add
	CONSTRAINT FK_ENCHERE_UTILISATEUR FOREIGN KEY (idUtilisateur)
	REFERENCES UTILISATEUR (idUtilisateur);
 
ALTER TABLE ENCHERE add
	CONSTRAINT FK_ENCHERE_ARTICLE FOREIGN KEY (idArticle)
	REFERENCES ARTICLE (idArticle);

ALTER TABLE ROLES add
	constraint FK_ROLES_UTILISATEURS foreign key (pseudo)
	references UTILISATEUR(pseudo);


ALTER TABLE CATEGORIE
ADD image VARCHAR(255);

UPDATE CATEGORIE SET image = 'clavier.jpg' WHERE idCategorie = 1;
UPDATE CATEGORIE SET image = 'chaise-en-bois.jpg' WHERE idCategorie = 2;
UPDATE CATEGORIE SET image = 'tondeuse.png' WHERE idCategorie = 3;
UPDATE CATEGORIE SET image = 'tennis.png' WHERE idCategorie = 4;
>>>>>>> b5df0ade690d028ea7fc81600bfb648f02434b8b
