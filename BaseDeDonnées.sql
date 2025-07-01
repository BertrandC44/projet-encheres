CREATE DATABASE [Encheres];

DROP TABLE [ENCHERE];
DROP TABLE [RETRAIT];
DROP TABLE [ARTICLE];
DROP TABLE [UTILISATEUR];
DROP TABLE [CATEGORIE];




CREATE TABLE [UTILISATEUR](
	[idUtilisateur] int  PRIMARY KEY IDENTITY,
	[pseudo] [NVARCHAR](15) NOT NULL,
	[nom] [NVARCHAR](15) NOT NULL,
	[prenom] [NVARCHAR](15) NOT NULL,
	[email] [NVARCHAR](25) NOT NULL,
	[telephone] [VARCHAR](10),
	[rue] [NVARCHAR](20) NOT NULL,
	[codePostal] VARCHAR (5) NOT NULL,
	[ville] [NVARCHAR](15) NOT NULL,
	[motDePasse] [NVARCHAR](15) NOT NULL,
	[credit] int NOT NULL,
	[administrateur] bit NOT NULL,
	);

	
CREATE TABLE [ARTICLE](
	[idArticle] int PRIMARY KEY IDENTITY,
	[nomArticle] [NVARCHAR](15) NOT NULL,
	[descritpion] [NVARCHAR](255) NOT NULL,
	[dateDebutEncheres] date NOT NULL,
	[dateFinEncheres] date NOT NULL,
	[miseAPrix] int NOT NULL,
	[prixVente] int NOT NULL,
	[etatVente] int NOT NULL,
	[idCategorie] int NOT NULL,
	[idUtilisateur] int NOT NULL,
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
	[libelle] [NVARCHAR](30) NOT NULL,
	)


CREATE TABLE [RETRAIT](
	[idArticle] int ,
	[rue] [NVARCHAR](20) NOT NULL,
	[codePostal] VARCHAR (5) NOT NULL,
	[ville] [NVARCHAR](15) NOT NULL,
	
	)



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
