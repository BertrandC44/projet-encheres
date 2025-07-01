-- Insertions utilisateurs

INSERT INTO UTILISATEUR (pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse, credit, administrateur) VALUES
('abaille', 'Baille', 'Annelise', 'abaille@campus-eni.fr', '0601020304', '10 rue des Lilas', '75000', 'Paris', '{bcrypt}$2a$10$KaSHgvH9p/cUnsOVPzYvzunWDAIv68whrOxmui1S.0AjzbP5RX7yO', 100, 0),
('sgobin', 'Gobin', 'Stephane', 'sgobin@campus-eni.fr', '0605060708', '15 rue du Bac', '69000', 'Lyon', '{bcrypt}$2a$10$B5U29ajHsIKd8aY3c/JNn.xTJpOCAeoXvT9XvfzbbHGP4iIFV9Lkm', 200, 1),
('jtrillard', 'Trillard', 'Julien', 'jtrillard@campus-eni.fr', '0608091011', '22 avenue Victor', '33000', 'Bordeaux', '{bcrypt}$2a$10$VwQ7gMUPLeQnFC6vCsOoTevzdPe.JPu0L/7cwPGdJ6TjSpipGwY.y', 150, 0),
('sdautais', 'Dautais', 'Servane', 'sdautais@campus-eni.fr', '0611121314', '5 boulevard Haussmann', '31000', 'Toulouse', '{bcrypt}$2a$10$qHGnMJYcTx1Vr.UTpnD.OuZHTbRS0O0N6SSn2BZMVbFekKOgjHTvu', 50, 0);

-- Insertions catégories

INSERT INTO CATEGORIE (libelle) VALUES
('Informatique'),
('Maison'),
('Jardinage'),
('Sport');

-- Insertions articles

INSERT INTO ARTICLE (nomArticle, description, dateDebutEncheres, dateFinEncheres, miseAPrix, prixVente, etatVente, idCategorie, idUtilisateur) VALUES
('Clavier', 'Clavier mécanique AZERTY', '2025-07-01', '2025-07-10', 50, 0, 0, 1, 1),
('Chaise', 'Chaise en bois massif', '2025-06-25', '2025-07-05', 30, 0, 0, 2, 2),
('Tondeuse', 'Tondeuse à gazon électrique', '2025-07-03', '2025-07-12', 100, 0, 0, 3, 3),
('Raquette', 'Raquette de tennis neuve', '2025-07-02', '2025-07-15', 70, 0, 0, 4, 4);

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