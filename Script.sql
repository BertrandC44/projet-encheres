-- Insertions utilisateurs

INSERT INTO UTILISATEUR (pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse, credit, administrateur) VALUES
('abaille', 'Baille', 'Annelise', 'abaille@campus-eni.fr', '0601020304', '10 rue des Lilas', '75000', 'Paris', 'Annelise10+', 100, 0),
('sgobin', 'Gobin', 'Stephane', 'sgobin@campus-eni.fr', '0605060708', '15 rue du Bac', '69000', 'Lyon', 'Stephane', 200, 1),
('jtrillard', 'Trillard', 'Julien', 'jtrillard@campus-eni.fr', '0608091011', '22 avenue Victor', '33000', 'Bordeaux', 'Julien', 150, 0),
('sdautais', 'Dautais', 'Servane', 'sdautais@campus-eni.fr', '0611121314', '5 boulevard Haussmann', '31000', 'Toulouse', 'Servane', 50, 0);

-- Insertions catégories

INSERT INTO CATEGORIE (libelle) VALUES
('Informatique'),
('Maison'),
('Jardinage'),
('Sport');

-- Insertions articles

INSERT INTO ARTICLE (nomArticle, description, dateDebutEncheres, dateFinEncheres, miseAPrix, prixVente, etatVente, idCategorie, idUtilisateur, imageArticle) VALUES
('Clavier', 'Clavier mécanique AZERTY', '2025-07-01', '2025-07-10', 50, 0, 0, 1, 1, 'clavier.jpg'),
('Chaise', 'Chaise en bois massif', '2025-06-25', '2025-07-05', 30, 0, 0, 2, 2, 'chaise-en-bois.jpg'),
('Tondeuse', 'Tondeuse à gazon électrique', '2025-07-03', '2025-07-12', 100, 0, 0, 3, 3, 'tondeuse.png'),
('Raquette', 'Raquette de tennis neuve', '2025-07-02', '2025-07-15', 70, 0, 0, 4, 4, 'tennis.png');

-- Insertions retraits

INSERT INTO RETRAIT (idArticle, rue, codePostal, ville) VALUES
(1, '10 rue des Lilas', '75000', 'Paris'),
(2, '15 rue du Bac', '69000', 'Lyon'),
(3, '22 avenue Victor', '33000', 'Bordeaux'),
(4, '5 boulevard Haussmann', '31000', 'Toulouse');

-- Insertions enchères

INSERT INTO ENCHERE (dateEnchere, montantEnchere, idUtilisateur, idArticle) VALUES
('2025-07-02', 55, 2, 1),  -- Stephane enchère sur Clavier
('2025-07-03', 60, 3, 1),  -- Julien enchère plus haute sur Clavier
('2025-06-26', 35, 1, 2),  -- Annelise sur Chaise
('2025-07-04', 110, 4, 3), -- Servane sur Tondeuse
('2025-07-05', 75, 1, 4);  -- Annelise sur Raquette

INSERT INTO [ROLES] ([pseudo],[role]) VALUES ('abaille','ROLE_ACQUEREUR');
INSERT INTO [ROLES] ([pseudo],[role]) VALUES ('abaille','ROLE_VENDEUR');
INSERT INTO [ROLES] ([pseudo],[role]) VALUES ('sgobin','ROLE_ACQUEREUR');
INSERT INTO [ROLES] ([pseudo],[role]) VALUES ('sgobin','ROLE_VENDEUR');
INSERT INTO [ROLES] ([pseudo],[role]) VALUES ('sgobin','ROLE_ADMIN');
INSERT INTO [ROLES] ([pseudo],[role]) VALUES ('jtrillard','ROLE_ACQUEREUR');
INSERT INTO [ROLES] ([pseudo],[role]) VALUES ('jtrillard','ROLE_VENDEUR');
INSERT INTO [ROLES] ([pseudo],[role]) VALUES ('sdautais','ROLE_ACQUEREUR');
INSERT INTO [ROLES] ([pseudo],[role]) VALUES ('sdautais','ROLE_VENDEUR');

