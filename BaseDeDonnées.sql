/*CREATE DATABASE [Encheres];*/

DROP TABLE [ENCHERE];
DROP TABLE [RETRAIT];
DROP TABLE [ARTICLE];
DROP TABLE [UTILISATEUR];
DROP TABLE [CATEGORIE];




CREATE TABLE [UTILISATEUR](
	[idUtilisateur] int  PRIMARY KEY IDENTITY,
	[pseudo] [NVARCHAR](150) NOT NULL,
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
	[dateEnchere] date NOT NULL,
	[montantEnchere] int NOT NULL,
	[idUtilisateur] int  NOT NULL,
	[idArticle] int NOT NULL,
	PRIMARY KEY ([idUtilisateur],[idArticle])
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



ALTER TABLE ARTICLE add
	CONSTRAINT FK_ARTICLE_UTILISATEUR FOREIGN KEY (idUtilisateur)
	REFERENCES UTILISATEUR(idUtilisateur)ON DELETE CASCADE;

ALTER TABLE ARTICLE add
	CONSTRAINT FK_ARTICLE_CATEGORIE FOREIGN KEY (idCategorie)
	REFERENCES CATEGORIE (idCategorie) ON DELETE CASCADE;

ALTER TABLE RETRAIT add
	CONSTRAINT FK_RETRAIT_ARTICLE FOREIGN KEY (idArticle)
	REFERENCES ARTICLE (idArticle) ON DELETE CASCADE;

ALTER TABLE ENCHERE add
	CONSTRAINT FK_ENCHERE_UTILISATEUR FOREIGN KEY (idUtilisateur)
	REFERENCES UTILISATEUR (idUtilisateur) ON DELETE CASCADE;

ALTER TABLE ENCHERE add
	CONSTRAINT FK_ENCHERE_ARTICLE FOREIGN KEY (idArticle)
	REFERENCES ARTICLE (idArticle) ON DELETE CASCADE;

