const ADMIN = "http://localhost:8080/AdminService";

/* ── NAV ── */
function show(name, btn) {
  document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
  document.querySelectorAll('.ni').forEach(n => n.classList.remove('active'));
  document.getElementById('section-' + name).classList.add('active');
  if (btn) btn.classList.add('active');
  document.getElementById('topbar-title').textContent = btn?.textContent?.trim() || name;
}

/* ── SOAP ── */
async function soap(body) {
  const r = await fetch(ADMIN, { method: 'POST', headers: { 'Content-Type': 'text/xml' }, body });
  return r.text();
}
const px = t => new DOMParser().parseFromString(t, 'text/xml');
const gt = (n, t) => { const e = n.getElementsByTagNameNS('*', t)[0]; return e ? e.textContent : '' };
const v = id => (document.getElementById(id)?.value || '').trim();
const es = s => String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');

function msg(id, text, type = 'ok') {
  const el = document.getElementById(id);
  el.className = 'msg ' + type;
  el.textContent = text;
}

function loader(id) {
  document.getElementById(id).innerHTML = `<div style="text-align:center;padding:1.5rem"><div class="spin"></div></div>`;
}

function errBox(t) {
  return `<div class="msg err" style="display:block">❌ ${t}</div>`;
}

/* ── REGISTER RESTAURANT ── */
function registerRestaurant() {
  const oid = v('ownerId'), name = v('rName'), addr = v('rAddress');
  if (!oid || !name || !addr) return msg('regMsg', 'Fill all fields.', 'err');
  msg('regMsg', '⏳ Registering…', 'inf');
  soap(`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:registerRestaurant>
      <ownerId>${oid}</ownerId><n>${es(name)}</n><address>${es(addr)}</address>
    </ws:registerRestaurant></soapenv:Body></soapenv:Envelope>`)
    .then(() => msg('regMsg', '✅ Restaurant registered!', 'ok'))
    .catch(() => msg('regMsg', '❌ Failed.', 'err'));
}

/* ── ADD MENU ITEM ── */
function addMenuItem() {
  const rid = v('restIdMenu'), name = v('itemName'), price = v('itemPrice'), qty = v('itemQty'), addons = v('itemAddons');
  if (!rid || !name || !price || !qty) return msg('menuMsg', 'Fill required fields.', 'err');
  msg('menuMsg', '⏳ Adding…', 'inf');
  soap(`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:addMenuItem>
      <restaurantId>${rid}</restaurantId><n>${es(name)}</n>
      <price>${price}</price><quantity>${qty}</quantity><addons>${es(addons)}</addons>
    </ws:addMenuItem></soapenv:Body></soapenv:Envelope>`)
    .then(() => msg('menuMsg', '✅ Menu item added!', 'ok'))
    .catch(() => msg('menuMsg', '❌ Failed.', 'err'));
}

/* ── UPDATE MENU ITEM ── */
function updateMenuItem() {
  const id = v('menuItemId'), qty = v('menuQty'), addons = v('menuAddons'), avail = v('menuAvail');
  if (!id) return msg('updateMsg', 'Enter a Menu Item ID.', 'err');
  msg('updateMsg', '⏳ Saving…', 'inf');
  const calls = [];
  if (qty) calls.push(soap(`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:setMenuQuantity><menuItemId>${id}</menuItemId><quantity>${qty}</quantity></ws:setMenuQuantity></soapenv:Body></soapenv:Envelope>`));
  if (addons) calls.push(soap(`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:setMenuAddons><menuItemId>${id}</menuItemId><addons>${es(addons)}</addons></ws:setMenuAddons></soapenv:Body></soapenv:Envelope>`));
  calls.push(soap(`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:setMenuAvailability><menuItemId>${id}</menuItemId><available>${avail}</available></ws:setMenuAvailability></soapenv:Body></soapenv:Envelope>`));
  Promise.all(calls)
    .then(() => msg('updateMsg', '✅ Updated!', 'ok'))
    .catch(() => msg('updateMsg', '❌ One or more updates failed.', 'err'));
}

/* ── DELETE MENU ITEM ── */
function deleteMenuItem() {
  const id = v('menuItemId');
  if (!id) return msg('updateMsg', 'Enter a Menu Item ID to delete.', 'err');
  if (!confirm(`Delete menu item #${id}? This cannot be undone.`)) return;
  soap(`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:deleteMenuItem><menuItemId>${id}</menuItemId></ws:deleteMenuItem></soapenv:Body></soapenv:Envelope>`)
    .then(() => msg('updateMsg', '✅ Item deleted!', 'ok'))
    .catch(() => msg('updateMsg', '❌ Failed to delete.', 'err'));
}

/* ── SET SCHEDULE ── */
function setSchedule() {
  const rid = v('schedRestId'), open = v('openTime'), close = v('closeTime');
  if (!rid || !open || !close) return msg('schedMsg', 'Fill all schedule fields.', 'err');
  soap(`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:setSchedule>
      <restaurantId>${rid}</restaurantId><openTime>${open}</openTime><closeTime>${close}</closeTime>
    </ws:setSchedule></soapenv:Body></soapenv:Envelope>`)
    .then(() => msg('schedMsg', '✅ Schedule saved!', 'ok'))
    .catch(() => msg('schedMsg', '❌ Failed.', 'err'));
}

/* ── GET SCHEDULE ── */
function getSchedule() {
  const rid = v('schedRestId'); if (!rid) return msg('schedMsg', 'Enter Restaurant ID.', 'err');
  soap(`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:getSchedule><restaurantId>${rid}</restaurantId></ws:getSchedule></soapenv:Body></soapenv:Envelope>`)
    .then(xml => {
      const s = px(xml).getElementsByTagNameNS('*', 'return')[0];
      const c = document.getElementById('schedResult');
      if (!s) { c.innerHTML = errBox('No schedule set for this restaurant.'); return; }
      c.innerHTML = `<div class="li" style="cursor:default">
      <div class="li-l"><div class="li-name">Restaurant #${rid} Schedule</div>
      <div class="li-sub">🟢 Opens: ${gt(s, 'openTime')} · 🔴 Closes: ${gt(s, 'closeTime')}</div></div>
      <span class="badge b-green">Active</span></div>`;
    })
    .catch(() => document.getElementById('schedResult').innerHTML = errBox('Failed to load schedule.'));
}

/* ── INCOMING ORDERS ── */
function loadOrdersByRestaurant() {
  const rid = v('ordRestId'); if (!rid) return;
  loader('rest-orders');
  soap(`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:getOrdersByRestaurant><restaurantId>${rid}</restaurantId></ws:getOrdersByRestaurant></soapenv:Body></soapenv:Envelope>`)
    .then(xml => {
      const list = px(xml).getElementsByTagNameNS('*', 'return');
      const c = document.getElementById('rest-orders');
      if (!list.length) { c.innerHTML = `<div class="empty"><div class="empty-icon">📭</div>No orders found.</div>`; return; }
      const rows = Array.from(list).map(o => {
        const status = gt(o, 'status');
        return `<tr>
        <td><strong>#${gt(o, 'id')}</strong></td>
        <td>Customer #${gt(o, 'customerId')}</td>
        <td>৳${parseFloat(gt(o, 'totalPrice') || 0).toFixed(2)}</td>
        <td><span class="badge ${statusBadge(status)}">${status.replace(/_/g, ' ')}</span></td>
      </tr>`;
      }).join('');
      c.innerHTML = `<div class="tbl-wrap"><table>
      <thead><tr><th>Order ID</th><th>Customer</th><th>Total</th><th>Status</th></tr></thead>
      <tbody>${rows}</tbody>
    </table></div>`;
    })
    .catch(() => document.getElementById('rest-orders').innerHTML = errBox('Failed to load orders.'));
}

/* ── LOAD RIDERS ── */
function loadRiders() {
  loader('riders-list');
  soap(`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:getAllRiders/></soapenv:Body></soapenv:Envelope>`)
    .then(xml => {
      const list = px(xml).getElementsByTagNameNS('*', 'return');
      const c = document.getElementById('riders-list');
      if (!list.length) { c.innerHTML = `<div class="empty"><div class="empty-icon">🛵</div>No riders registered.</div>`; return; }
      c.innerHTML = Array.from(list).map(r => {
        const avail = gt(r, 'available') === 'true';
        return `<div class="li">
        <div class="li-l">
          <div class="li-name"><span class="rider-dot ${avail ? 'available' : 'busy'}"></span> ${gt(r, 'name')}</div>
          <div class="li-sub">${avail ? '🟢 Available for delivery' : '🔴 Currently on delivery'}</div>
        </div>
        <div class="li-r">
          <span class="badge ${avail ? 'b-green' : 'b-red'}">${avail ? 'Available' : 'Busy'}</span>
          <span class="badge b-blue">ID ${gt(r, 'id')}</span>
        </div>
      </div>`;
      }).join('');
    })
    .catch(() => document.getElementById('riders-list').innerHTML = errBox('Failed to load riders.'));
}

/* ── ASSIGN RIDER ── */
function assignRider() {
  const oid = v('orderId'), rid = v('riderId');
  if (!oid || !rid) return msg('riderMsg', 'Enter both Order ID and Rider ID.', 'err');
  msg('riderMsg', '⏳ Dispatching…', 'inf');
  soap(`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:assignRiderToOrder>
      <orderId>${oid}</orderId><riderId>${rid}</riderId>
    </ws:assignRiderToOrder></soapenv:Body></soapenv:Envelope>`)
    .then(() => { msg('riderMsg', '✅ Rider dispatched!', 'ok'); loadRiders(); })
    .catch(() => msg('riderMsg', '❌ Dispatch failed.', 'err'));
}

/* ── PAYMENTS ── */
function loadPayments() {
  const rid = v('payRestId'); if (!rid) return;
  loader('payments-container');
  soap(`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:getPaymentsByRestaurant><restaurantId>${rid}</restaurantId></ws:getPaymentsByRestaurant></soapenv:Body></soapenv:Envelope>`)
    .then(xml => {
      const list = px(xml).getElementsByTagNameNS('*', 'return');
      const c = document.getElementById('payments-container');
      if (!list.length) { c.innerHTML = `<div class="empty"><div class="empty-icon">💸</div>No payments found.</div>`; return; }
      const rows = Array.from(list).map(p => {
        const status = gt(p, 'status'), method = gt(p, 'method');
        return `<tr>
        <td><strong>#${gt(p, 'id')}</strong></td>
        <td>Order #${gt(p, 'orderId') || '—'}</td>
        <td><span class="badge ${method === 'CARD' ? 'b-blue' : 'b-amber'}">${method}</span></td>
        <td><span class="badge ${status === 'PAID' ? 'b-green' : 'b-gray'}">${status}</span></td>
      </tr>`;
      }).join('');
      c.innerHTML = `<div class="tbl-wrap"><table>
      <thead><tr><th>Payment ID</th><th>Order</th><th>Method</th><th>Status</th></tr></thead>
      <tbody>${rows}</tbody>
    </table></div>`;
    })
    .catch(() => document.getElementById('payments-container').innerHTML = errBox('Failed to load payments.'));
}

/* ── ADD COUPON ── */
function addCoupon() {
  const code = v('couponCode'), disc = v('couponDiscount');
  if (!code || !disc) return msg('couponMsg', 'Enter code and discount %.', 'err');
  if (parseInt(disc) < 1 || parseInt(disc) > 100) return msg('couponMsg', 'Discount must be 1–100%.', 'err');
  msg('couponMsg', '⏳ Creating…', 'inf');
  soap(`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:addCoupon>
      <code>${es(code.toUpperCase())}</code><discountPercent>${disc}</discountPercent>
    </ws:addCoupon></soapenv:Body></soapenv:Envelope>`)
    .then(() => { msg('couponMsg', `✅ Coupon "${code.toUpperCase()}" created!`, 'ok'); loadCoupons(); })
    .catch(() => msg('couponMsg', '❌ Failed to create coupon.', 'err'));
}

/* ── LOAD COUPONS ── */
function loadCoupons() {
  loader('coupons-list');
  soap(`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:getAllCoupons/></soapenv:Body></soapenv:Envelope>`)
    .then(xml => {
      const list = px(xml).getElementsByTagNameNS('*', 'return');
      const c = document.getElementById('coupons-list');
      if (!list.length) { c.innerHTML = `<div class="empty"><div class="empty-icon">🏷️</div>No coupons yet.</div>`; return; }
      c.innerHTML = Array.from(list).map(cp => `
      <div class="li">
        <div class="li-l">
          <div class="li-name">🏷️ ${gt(cp, 'code')}</div>
          <div class="li-sub">Saves ${gt(cp, 'discountPercent')}% on the order total</div>
        </div>
        <div class="li-r">
          <span class="badge b-gold">${gt(cp, 'discountPercent')}% OFF</span>
          <span class="badge b-blue">ID ${gt(cp, 'id')}</span>
        </div>
      </div>`).join('');
    })
    .catch(() => document.getElementById('coupons-list').innerHTML = errBox('Failed to load coupons.'));
}

/* ── UTILS ── */
function statusBadge(s) {
  return { PLACED: 'b-gold', PAID: 'b-green', OUT_FOR_DELIVERY: 'b-amber', DELIVERED: 'b-green', CANCELLED: 'b-red' }[s] || 'b-gray';
}