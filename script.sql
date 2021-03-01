

---------------------
---  SID_VENTE    ---
---------------------
insert into sid_vente(fac_num, type_support, date_vente, date_sortie_album, ar_id, al_id, nb_artistes, quantite,prix_unitaire,CA)
select distinct f.fac_num, p.prod_type_support, fac_date, al_sortie,10, al_id,
                ( select count(*) from sio_interprete i
                                           inner join sio_contient c on c.en_id = i.en_id
                  where prod_al = c.al_id ) as nb_artistes,
                ( select count(*) from sio_facture f2
                                           inner join sio_ligne_facture l2 on f2.fac_num = l2.lig_facture
                                           inner join sio_produit p2 on l2.lig_produit = p2.prod_id
                                           inner join sio_album a2 on a2.al_id = p2.prod_al
                  where f2.fac_num = f.fac_num and a.al_id = a2.al_id
                    and p.prod_type_support = p2.prod_type_support
                ) as quantite,
                l.lig_prix_vente,
                ( select count(*) from sio_facture f2
                                           inner join sio_ligne_facture l2 on f2.fac_num = l2.lig_facture
                                           inner join sio_produit p2 on l2.lig_produit = p2.prod_id
                                           inner join sio_album a2 on a2.al_id = p2.prod_al
                  where f2.fac_num = f.fac_num and a.al_id = a2.al_id
                    and p.prod_type_support = p2.prod_type_support
                ) * l.lig_prix_vente as CA
from sio_facture f
         inner join sio_ligne_facture l on f.fac_num = l.lig_facture
         inner join sio_produit p on l.lig_produit = p.prod_id
         inner join sio_album a on a.al_id = p.prod_al;