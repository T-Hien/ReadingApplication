package vn.example.readingapplication.retrofit

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import vn.example.readingapplication.model.Author
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.BookCategory
import vn.example.readingapplication.model.Bookmarks
import vn.example.readingapplication.model.Category
import vn.example.readingapplication.model.Chapter
import vn.example.readingapplication.model.DetailAuthor
import vn.example.readingapplication.model.Favorite
import vn.example.readingapplication.model.Like
import vn.example.readingapplication.model.Note
import vn.example.readingapplication.model.Notification
import vn.example.readingapplication.model.ReadingProgress
import vn.example.readingapplication.model.Replies
import vn.example.readingapplication.model.Search
import vn.example.readingapplication.model.Setting
import vn.example.readingapplication.model.User
import vn.example.readingapplication.model.search.BookSearch
import vn.example.readingapplication.model.statics.AuthorFavorite
import vn.example.readingapplication.model.statics.CategoryFavorite
import vn.example.readingapplication.model.statics.ReadingStatic

interface ApiService {
    @PUT("repos/{owner}/{repo}/contents/{path}")
    fun uploadImage(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String,
        @Body body: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseBody>
    @DELETE("repos/{owner}/{repo}/contents/{path}")
    fun deleteFile(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String,
        @Body body: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("user/sign-in")
    fun signIn(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ResultResponse<User>>

    @POST("user/create")
    fun createUser(@Body userDTO: User): Call<ResultResponse<User>>
    @GET("user/all")
    fun getAllUser2(): Call<ResultResponse<List<User>>>

    @GET("user/findAll/role")
    fun getAllUser(@Query("role")role:Int):Call<ResultResponse<List<User>>>

    @GET("user/{username}")
    fun getUser(
        @Path("username") username:String
    ):Call<ResultResponse<User>>

    @GET("user/count")
    fun getCountUserByRole(@Query("role") role:Int):Call<ResultResponse<Int>>


    //BOOK
    @GET("book/all")
    fun getAllBooks(): Call<ResultResponse<Book>>

    @GET("book/search/username/{username}/type/{type}")
    fun getSearchAll(
        @Path("username") username:String,
        @Path("type") type:String
    ):Call<ResultResponse<Book>>

    @GET("book/latest")
    fun getNewBooks():Call<ResultResponse<Book>>


    @GET("book/author/{authorId}")
    fun getSearchAuthorListBook(@Path("authorId") authorId: Int): Call<ResultResponse<Book>>
    @GET("book/category")
    fun getCategoryListBookAdmin(@Query("categoryId") categoryId: Int): Call<ResultResponse<Book>>

    @GET("book/author")
    fun getAuthorListBookAdmin(@Query("authorId") authorId: Int): Call<ResultResponse<Book>>

    @GET("book/search")
    fun getSearchBook(@Query("keyword") keyword: String): Call<ResultResponse<Book>>

    @GET("book/bookId")
    fun getReadBook(@Query("bookId") bookId: Int): Call<ResultResponse<Book>>

    @GET("book/id")
    fun getBookById(@Query("bookId") bookId: Int): Call<ResultResponse<Book>>

    @GET("book/all3")
    fun getAllBooksAd(@Query("active") active:Int): Call<ResultResponse<BookSearch>>

    @GET("book/all3")
    fun getAllBooksAd2(@Query("active") active:Int): Call<ResultResponse<Book>>
    @POST("book/create")
    fun createBook(@Body book:Book):Call<ResultResponse<Book>>

    @POST("book/update")
    fun updateBook(@Body book:Book):Call<ResultResponse<Book>>

    @POST("book/delete")
    fun deleteBook(@Body book:Book):Call<ResultResponse<Book>>

    @POST("book/deleteByBookId")
    fun deleteBookById(@Body requestBody: Map<String, Int>): Call<ResultResponse<Void>>

    @GET("book/count")
    fun getCountBook():Call<ResultResponse<Int>>

    //BOOKMARK
    @GET("bookmark/book/{username}")
    fun getBookByBookId(
        @Path("username") bookId:Int
    ): Call<ResultResponse<Book>>

    @GET("bookmark/{username}")
    fun getBookmark(
        @Path("username") username: String
        ): Call<ResultResponse<Bookmarks>>

    @GET("bookmark/get")
    fun getBookmarkByBook(
        @Query("chapternum") chapternum: Int,
        @Query("type") type: String,
        @Query("bookId") bookId: Int,
        @Query("username") username: String
    ): Call<ResultResponse<Bookmarks>>

    @POST("bookmark/deleteByBookId")
    fun deleteBookmark(@Body requestBody: Map<String, Int>): Call<ResultResponse<Void>>
    @POST("bookmark/delete")
    fun deleteBookmarkById(@Body requestBody: Map<String, Int>): Call<ResultResponse<Void>>
    @POST("bookmark/deleteByNote")
    fun deleteBookmarkByNoteId(@Body requestBody: Map<String, Int>): Call<ResultResponse<Void>>

    @POST("bookmark/create")
    fun createBookmark(@Body bookmarks: Bookmarks):Call<ResultResponse<Bookmarks>>

    //BOOKCATEGORY

    @GET("bookcategory/category/{categoryId}")
    fun getBookByCategoryId(
        @Path("categoryId") categoryId:Int

    ):Call<ResultResponse<BookCategory>>
    @GET("bookcategory/all")
    fun getAllBookCategory():Call<ResultResponse<List<BookCategory>>>

    @POST("bookcategory/create")
    fun createBookCategory(@Body bookcategory:BookCategory):Call<ResultResponse<BookCategory>>

    @POST("bookcategory/delete")
    fun deleteBookCategory(@Body bookcategory:BookCategory):Call<ResultResponse<BookCategory>>

    @POST("bookcategory/deleteByBookId")
    fun deleteAllBookCategory(@Body requestBody: Map<String, Int>): Call<ResultResponse<Void>>

    //FAVORITE
    @GET("favorite/top20")
    fun getFavoriteBooks():Call<ResultResponse<Favorite>>

    @POST("favorite/create")
    fun createFavorite(@Body favorite: Favorite):Call<ResultResponse<Favorite>>

    @GET("favorite/{bookId}")
    fun getFavorite(@Path("bookId")bookId:Int):Call<ResultResponse<Favorite>>

    @POST("favorite/deleteByBookId")
    fun deleteFavorite(@Body requestBody: Map<String, Int>): Call<ResultResponse<Void>>

    //CATEGORY
    @GET("category/all")
    fun getListCategory():Call<ResultResponse<Category>>

    @POST("category/create")
    fun createCategory(@Body category:Category):Call<ResultResponse<Category>>

    @POST("category/update")
    fun updateCategory(@Body category:Category):Call<ResultResponse<Category>>

    @Headers("Content-Type: application/json")
    @POST("category/delete")
    fun deleteCategory(@Body category:Category):Call<ResultResponse<Category>>

    @GET("category/search")
    fun getSearchCategory(@Query("keyword") keyword: String): Call<ResultResponse<Category>>
    @GET("category/favorites")
    fun getStaticCategory(): Call<ResultResponse<CategoryFavorite>>

    @GET("category/count")
    fun getCountCategory():Call<ResultResponse<Int>>


    //SEARCH
    @GET("search/username/{username}/type/{type}")
    fun getSearchType(
        @Path("username") username:String,
        @Path("type") type:String
    ):Call<ResultResponse<Search>>

    @POST("search/create")
    fun createSearch(@Body search:Search): Call<ResultResponse<Search>>

    //AUTHOR
    @GET("author/search")
    fun getSearchAuthor(@Query("keyword") keyword: String): Call<ResultResponse<Author>>
    @GET("author/all")
    fun getAllAuthor():Call<ResultResponse<Author>>
    @GET("author/item")
    fun getAuthorById(@Query("id")id:Int):Call<ResultResponse<Author>>

    @POST("author/create")
    fun createAuthor(@Body author:Author):Call<ResultResponse<Author>>

    @Headers("Content-Type: application/json")
    @DELETE("author/delete")
    fun deleteAuthor(@Body author:Author):Call<ResultResponse<Author>>

    @POST("author/delete")
    fun deleteAuthor2(@Body author:Author):Call<ResultResponse<Author>>

    @GET("author/favorites")
    fun getStaticAuthor(): Call<ResultResponse<AuthorFavorite>>

    @GET("author/count")
    fun getCountAuthor():Call<ResultResponse<Int>>

    @GET("reading/username/{username}")
    fun getBookReadingAll(
        @Path("username") username:String
    ):Call<ResultResponse<List<ReadingProgress>>>
    //Library 2 3
    @GET("reading/username/{username}/status/{status}")
    fun getBookReadingByStatus(
        @Path("username") username:String,
        @Path("status") status:String
    ):Call<ResultResponse<List<ReadingProgress>>>

    //Bo
    @GET("reading/username/{username}/bookId/{bookId}")
    fun getReading2(
        @Path("username") username:String,
        @Path("bookId") bookId:Int
    ):Call<ResultResponse<ReadingProgress>>

    @GET("reading/getChapter")
    fun getReading(
        @Query("username") username:String,
        @Query("bookId") bookId:Int,
        @Query("chapnum") chapnum:Int
    ):Call<ResultResponse<ReadingProgress>>

    @POST("reading/create")
    fun createReading(@Body readingProgress: ReadingProgress):Call<ResultResponse<ReadingProgress>>

    @POST("reading/delete")
    fun deleteReading(@Body readingProgress: ReadingProgress):Call<ResultResponse<ReadingProgress>>

    @POST("reading/deleteByBookId")
    fun deleteAllReadingProgress(@Body requestBody: Map<String, Int>): Call<ResultResponse<Void>>

    @GET("reading/static")
    fun getStaticReading(): Call<ResultResponse<ReadingStatic>>


    //CHAPTER
    @GET("chapter/chapterNum/{chapterNum}/bookId/{bookId}")
    fun getChapterByIdAndBookId(
        @Path("chapterNum") chapterNum:Int,
        @Path("bookId") bookId:Int
    ):Call<ResultResponse<Chapter>>

    @GET("chapter/bookId/{bookId}")
    fun getChapter(
        @Path("bookId") bookId:Int
    ):Call<ResultResponse<Chapter>>

    @POST("chapter/create")
    fun createChapter(@Body chapter: Chapter):Call<ResultResponse<Chapter>>

    @POST("chapter/deleteById")
    fun deleteAllChapterByBook(@Body requestBody: Map<String, Int>): Call<ResultResponse<Void>>


    //Lấy tin comment
    @GET("note/type")
    fun getCommentByType(
        @Query("bookId") bookId:Int,
        @Query("type") type:String
    ):Call<ResultResponse<Note>>

    //Lấy comment by chap
    @GET("note/chap")
    fun getCommentByChap(
        @Query("bookId") bookId:Int,
        @Query("type") type:String,
        @Query("chapnum") chapnum:Int
    ):Call<ResultResponse<Note>>

    @POST("note/create")
    fun createNote(@Body note: Note): Call<ResultResponse<Note>>
    @POST("note/update")
    fun updateNote(@Body note: Note): Call<ResultResponse<Note>>

    @GET("note/getnotebychap")
    fun getNoteByChap(
        @Query("type") type:String,
        @Query("bookId") bookId: Int,
        @Query("username") username: String,
        @Query("chapnum") chapternum: Int,
    ): Call<ResultResponse<Note>>
    @GET("note/{type}/{bookId}/{username}")
    fun getNoteByBook(
        @Path("type") type:String,
        @Path("bookId") bookId: Int,
        @Path("username") username: String
    ): Call<ResultResponse<Note>>
    @POST("note/delete")
    fun deleteNote(@Body note:Note):Call<ResultResponse<Note>>

    @GET("note/byType")
    fun getAllNote(@Query("type")type:String):Call<ResultResponse<Note>>

    @GET("note/byId")
    fun getNoteById(@Query("noteId")noteId:Int):Call<ResultResponse<Note>>

    @GET("note/byTypeAndStatus")
    fun getAllNoteByStatus(@Query("type")type:String,@Query("status") status:Int):Call<ResultResponse<Note>>

    @POST("note/deleteByBookId")
    fun deleteAllNoteByBook(@Body requestBody: Map<String, Int>): Call<ResultResponse<Void>>

    @POST("note/deleteById")
    fun deleteNoteById(@Body requestBody: Map<String, Int>): Call<ResultResponse<Void>>
    @POST("note/deleteByNoteId")
    fun deleteNoteByNoteId(@Body requestBody: Map<String, Int>): Call<ResultResponse<Void>>


    //Setting
    @GET("setting/{username}")
    fun getSetting(
        @Path("username") username:String
    ):Call<ResultResponse<Setting>>

    @POST("setting/create")
    fun createSetting(@Body setting:Setting): Call<ResultResponse<Setting>>

    //Tạo Bình luận

    //Notification
    @GET("notification/all")
    fun getNotification():Call<ResultResponse<Notification>>

    @POST("notification/create")
    fun createNotification(@Body notification: Notification):Call<ResultResponse<Notification>>

    @POST("notification/update")
    fun updateNotification(@Body notification: Notification):Call<ResultResponse<Notification>>

    @POST("notification/deleteByBookId")
    fun deleteAllNotification(@Body requestBody: Map<String, Int>): Call<ResultResponse<Void>>


    //DetailAuthor
    @POST("detailauthor/create")
    fun createDetailAuthor(@Body detail:DetailAuthor):Call<ResultResponse<DetailAuthor>>

    @POST("detailauthor/delete")
    fun deleteDetailAuthor(@Body detail:DetailAuthor):Call<ResultResponse<DetailAuthor>>

    @POST("detailauthor/deleteById")
    fun deleteAllDetailAuthor(@Body requestBody: Map<String, Int>): Call<ResultResponse<Void>>

    //Like
    @GET("like/find")
    fun findLike(@Query("bookId") bookId:Int, @Query("username") username:String):Call<ResultResponse<Like>>
    @POST("like/create")
    fun createLike(@Body like:Like):Call<ResultResponse<Like>>
    @POST("like/deleteByBookId")
    fun deleteLikeByBookId(@Body requestBody: Map<String, Int>): Call<ResultResponse<Void>>
    @POST("like/delete")
    fun deleteLike(@Query("bookId") bookId:Int, @Query("username") username:String):Call<ResultResponse<Like>>

    @POST("like/deleteByBookId")
    fun deleteLikeByBook(@Body requestBody: Map<String, Int>): Call<ResultResponse<Void>>

    @GET("like/all")
    fun findAllLike():Call<ResultResponse<Like>>

    @GET("like/findByBook")
    fun findListLikeByBook(@Query("bookId") bookId:Int):Call<ResultResponse<Like>>
    @GET("like/findByUser")
    fun findListLikeByUser(@Query("username") username:String):Call<ResultResponse<Like>>

    //Replies
    @POST("replies/create")
    fun createReplies(@Body reply: Replies):Call<ResultResponse<Replies>>

    @POST("replies/delete")
    fun deleteReplies(@Body reply:Replies):Call<ResultResponse<Replies>>
    @POST("replies/deletebyNote")
    fun deleteRepliesByNote(@Body reply:Replies):Call<ResultResponse<Replies>>

    @POST("replies/deleteByBookId")
    fun deleteAllRepliesByNoteId(@Body requestBody: Map<String, Int>): Call<ResultResponse<Void>>

}
