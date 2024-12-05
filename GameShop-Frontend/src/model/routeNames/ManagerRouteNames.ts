export enum ManagerRouteNames {
  DASHBOARD = '/manager/dashboard',
  EMPLOYEES = '/manager/employees',
  EMPLOYEE_DETAILS = '/employees/:id',
  CREATE_EMPLOYEE = '/employees/create',
  PROMOTIONS = '/manager/promotions',
  CREATE_PROMOTION = '/manager/promotions/create',
  REVIEWS = '/manager/reviews',
  REVIEW_DETAILS = '/manager/reviews',
  GAME_REQUESTS = '/manager/game-requests',
  GAME_REQUEST_DETAIL = '/manager/game-requests/:id',
  GAMES = '/manager/games',
  CREATE_GAME = '/manager/games/create',
  GAME_DETAIL = '/manager/games/:id',
  CATEGORIES = '/manager/categories',
  CREATE_CATEGORY = '/manager/categories/create',
  CATEGORY_DETAIL = '/manager/categories/:id',
  PROMOTED_GAMES = 'promotions/:id/items'
}
