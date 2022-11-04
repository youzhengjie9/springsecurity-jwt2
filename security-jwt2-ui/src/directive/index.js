import hasPerm from './permission/has-perm'

const install = function (Vue) {
      Vue.directive('hasPerm', hasPerm)
}
if (window.Vue) {
      window['hasPerm'] = hasPerm
      Vue.use(install); // eslint-disable-line
}
export default install
 