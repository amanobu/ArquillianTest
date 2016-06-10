package ama;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author ama
 */
@RunWith(Arquillian.class)
public class GameTest {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(Game.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

    }
    private static final String[] GAME_TITLES = {
        "Super Mario Brothers",
        "Mario Kart",
        "F-Zero"
    };

    @PersistenceContext
    EntityManager em;

    @Inject
    UserTransaction utx;

    @Before
    public void preparePersistenceTest() throws Exception {
        clearData();
        insertData();
        startTransaction();
    }

    private void clearData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Dumping old records...");
        em.createQuery("delete from Game").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String title : GAME_TITLES) {
            Game game = new Game(title);
            em.persist(game);
        }
        utx.commit();
    }

    private void startTransaction() throws Exception {
        utx.begin();
        em.joinTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        utx.commit();
    }

    // テストはここから
    @Test
    public void shouldFindAllGamesUsiongJpqlQuery() throws Exception {
        String fetchAllGamesInJpql = "select g from Game g order by g.id";
        List<Game> games = em.createQuery(fetchAllGamesInJpql, Game.class).getResultList();
        assertContainsAllGames(games);
    }

    private static void assertContainsAllGames(Collection<Game> games) {
        Assert.assertEquals(games.size(), GAME_TITLES.length);
        final Set<String> retrievedGameTitles = new HashSet<>();
        for (Game game : games) {
            System.out.println("* " + game);
            retrievedGameTitles.add(game.getTitle());
        }
        Assert.assertTrue(retrievedGameTitles.containsAll(Arrays.asList(GAME_TITLES)));
    }

    @Test
    public void shouldFindAllGamesUsingCriteriaApi() throws Exception {
        // given
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Game> criteria = builder.createQuery(Game.class);

        Root<Game> game = criteria.from(Game.class);
        criteria.select(game);
        // TIP: もしJPA 2メタモデルを使いたくない場合は、
        // この get() メソッドコールを get("id") に変更できます
        criteria.orderBy(builder.asc(game.get(Game_.id)));
        // WHERE句が無いので、すべてのselectを意味します

        // when
        System.out.println("Selecting (using Criteria)...");
        List<Game> games = em.createQuery(criteria).getResultList();

        // then
        System.out.println("Found " + games.size() + " games (using Criteria):");
        assertContainsAllGames(games);
    }

    @Test
    public void sqltest() throws Exception {
        //新しいゲームを追加
        Game game = new Game("Super Mario Maker");
        em.persist(game);
        dump();//全データ出力
        
        //Sper Mario Makerのタイトルを検索
        String squery = "select g from Game g where g.title = :title";
        Query query = em.createQuery(squery);
        query.setParameter("title", "Super Mario Maker");
        Game selectgame = (Game)query.getSingleResult();
        
        //insertしたのであるはずse
        Assert.assertEquals("Super Mario Maker", selectgame.getTitle());
        
        //それを消す
        em.remove(selectgame);
        selectgame = em.find(Game.class, selectgame.getId());
        //削除したので無いはず
        Assert.assertNull(selectgame);
    }

    public void dump() throws Exception {
        String query = "select g from Game g";
        List<Game> games = em.createQuery(query, Game.class).getResultList();
        System.out.println("all:"+games);
    }
}
