export enum ManagerRouteNames {
  DASHBOARD = '/manager/dashboard',
<<<<<<< HEAD
  GAME_REQUESTS = '/manager/game-requests',
  GAME_REQUEST_DETAIL = '/manager/game-requests/:id', //*:id is a placeholder for the game request ID
  GAMES = '/manager/games',
  CREATE_GAME = '/manager/games/create',
  GAME_DETAIL = '/manager/games/:id',
  CATEGORIES = '/manager/categories',
  CREATE_CATEGORY = '/manager/categories/create',
  CATEGORY_DETAIL = '/manager/categories/:id',
=======
>>>>>>> d116727 (first iter)
}
//* will use <Link to={`${ManagerRouteNames.GAME_REQUEST_DETAIL.replace(':id', request.id)}`}> title or whatever </Link>
