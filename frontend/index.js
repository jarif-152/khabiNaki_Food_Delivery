const R_URL = "http://localhost:8080/RestaurantService";
const O_URL = "http://localhost:8080/OrderService";

/* ── PAGES ── */
function showPage(name, btn) {
  document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
  document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
  document.getElementById('page-' + name).classList.add('active');
  if (btn) btn.classList.add('active');
}

/* ── SOAP CORE ── */
async function soap(url, body) {
  const r = await fetch(url, { method: 'POST', headers: { 'Content-Type': 'text/xml' }, body });
  return r.text();
}
const px = t => new DOMParser().parseFromString(t, 'text/xml');
const gt = (n, t) => { const e = n.getElementsByTagNameNS('*', t)[0]; return e ? e.textContent : ''; };
const v = id => (document.getElementById(id)?.value || '').trim();
const es = s => String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');

function msg(id, text, type = 'ok') {
  const el = document.getElementById(id);
  el.className = 'msg ' + type; el.textContent = text;
}
function loader(cont) {
  document.getElementById(cont).innerHTML = `<div style="text-align:center;padding:1.5rem"><div class="spin"></div></div>`;
}

/* ── ALL RESTAURANTS ── */
function loadAllRestaurants() {
  loader('all-restaurants');
  soap(R_URL, `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:getAllRestaurants/></soapenv:Body></soapenv:Envelope>`)
    .then(xml => renderRestaurants(xml, 'all-restaurants'))
    .catch(() => document.getElementById('all-restaurants').innerHTML = errBox('Failed to load restaurants.'));
}

/* ── SEARCH RESTAURANTS ── */
function searchRestaurants() {
  const area = v('area'); if (!area) return;
  loader('search-restaurants');
  soap(R_URL, `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:getRestaurantsByArea><area>${es(area)}</area></ws:getRestaurantsByArea></soapenv:Body></soapenv:Envelope>`)
    .then(xml => renderRestaurants(xml, 'search-restaurants'))
    .catch(() => document.getElementById('search-restaurants').innerHTML = errBox('Search failed.'));
}

function renderRestaurants(xml, containerId) {
  const list = px(xml).getElementsByTagNameNS('*', 'return');
  const c = document.getElementById(containerId);
  if (!list.length) { c.innerHTML = `<div class="empty"><div class="empty-icon">🏪</div>No restaurants found.</div>`; return; }
  c.innerHTML = Array.from(list).map(r => `
    <div class="rest-card" onclick="pickRestaurant('${gt(r, 'id')}')">
      <div class="li-left">
        <div class="li-name">🏪 ${gt(r, 'name')}</div>
        <div class="li-sub">📍 ${gt(r, 'address')}</div>
      </div>
      <div class="li-right">
        <span class="badge b-gold">ID ${gt(r, 'id')}</span>
        <span style="color:var(--muted);font-size:.8rem">→</span>
      </div>
    </div>`).join('');
}

function pickRestaurant(id) {
  document.getElementById('restaurantId').value = id;
  document.getElementById('ord-restId').value = id;
  loadMenu();
}

/* ── MENU ── */
function loadMenu() {
  const id = v('restaurantId'); if (!id) return;
  loader('menu-list');
  soap(R_URL, `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:getMenuByRestaurant><restaurantId>${id}</restaurantId></ws:getMenuByRestaurant></soapenv:Body></soapenv:Envelope>`)
    .then(xml => {
      const list = px(xml).getElementsByTagNameNS('*', 'return');
      const c = document.getElementById('menu-list');
      if (!list.length) { c.innerHTML = `<div class="empty"><div class="empty-icon">🍽️</div>No items available.</div>`; return; }
      c.innerHTML = Array.from(list).map(m => {
        const qty = parseInt(gt(m, 'quantity') || '0');
        return `<div class="menu-card">
        <div>
          <div class="li-name">🍱 ${gt(m, 'name')}</div>
          <div class="li-sub" style="margin-top:.15rem">${gt(m, 'addons') || 'No add-ons'} · Qty: ${qty}</div>
        </div>
        <div class="li-right">
          <strong style="color:var(--gold);font-size:.98rem">৳${parseFloat(gt(m, 'price')).toFixed(2)}</strong>
          <span class="badge b-gold">ID ${gt(m, 'id')}</span>
          ${qty > 0 ? '<span class="badge b-green">In Stock</span>' : '<span class="badge b-red">Out</span>'}
        </div>
      </div>`;
      }).join('');
    })
    .catch(() => document.getElementById('menu-list').innerHTML = errBox('Failed to load menu.'));
}

/* ── PLACE ORDER ── */
function placeOrder() {
  const cid = v('customerId'), rid = v('ord-restId'), mid = v('menuItemId'), qty = v('qty'), coup = v('coupon');
  if (!cid || !rid || !mid || !qty) return msg('orderResult', 'Please fill all required fields.', 'err');
  msg('orderResult', '⏳ Placing order…', 'inf');
  soap(O_URL, `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:placeOrder>
      <customerId>${cid}</customerId><restaurantId>${rid}</restaurantId>
      <menuItemId>${mid}</menuItemId><quantity>${qty}</quantity>
      <couponCode>${es(coup)}</couponCode>
    </ws:placeOrder></soapenv:Body></soapenv:Envelope>`)
    .then(xml => {
      const oid = px(xml).getElementsByTagNameNS('*', 'return')[0]?.textContent;
      oid ? msg('orderResult', `✅ Order placed! Order ID: ${oid}`, 'ok')
        : msg('orderResult', '❌ Failed to place order.', 'err');
    }).catch(() => msg('orderResult', '❌ Connection error.', 'err'));
}

/* ── PAY ── */
function payOrder() {
  const oid = v('orderIdPay'), method = v('payMethod');
  if (!oid) return msg('payResult', 'Enter Order ID.', 'err');
  soap(O_URL, `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:payOrder><orderId>${oid}</orderId><method>${method}</method></ws:payOrder></soapenv:Body></soapenv:Envelope>`)
    .then(() => msg('payResult', '✅ Payment confirmed!', 'ok'))
    .catch(() => msg('payResult', '❌ Payment failed.', 'err'));
}

/* ── ASSIGN RIDER ── */
function assignRider() {
  const oid = v('orderIdPay'); if (!oid) return msg('payResult', 'Enter Order ID.', 'err');
  soap(O_URL, `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:assignRider><orderId>${oid}</orderId></ws:assignRider></soapenv:Body></soapenv:Envelope>`)
    .then(() => msg('payResult', '✅ Rider assigned! Your order is on the way.', 'ok'))
    .catch(() => msg('payResult', '❌ No riders available right now.', 'err'));
}

/* ── PROGRESS ── */
function updateProgress() {
  const oid = v('orderIdProgress'), prog = v('progressStatus');
  if (!oid) return msg('progressResult', 'Enter Order ID.', 'err');
  soap(O_URL, `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:updateDeliveryProgress>
      <orderId>${oid}</orderId><progress>${prog}</progress>
    </ws:updateDeliveryProgress></soapenv:Body></soapenv:Envelope>`)
    .then(() => msg('progressResult', '✅ Delivery status updated!', 'ok'))
    .catch(() => msg('progressResult', '❌ Update failed.', 'err'));
}

/* ── ORDER HISTORY ── */
function loadOrderHistory() {
  const cid = v('historyCustomerId'); if (!cid) return;
  loader('order-history');
  soap(O_URL, `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:getOrdersByCustomer><customerId>${cid}</customerId></ws:getOrdersByCustomer></soapenv:Body></soapenv:Envelope>`)
    .then(xml => {
      const list = px(xml).getElementsByTagNameNS('*', 'return');
      const c = document.getElementById('order-history');
      if (!list.length) { c.innerHTML = `<div class="empty"><div class="empty-icon">📭</div>No orders found for this customer.</div>`; return; }
      c.innerHTML = Array.from(list).map(o => {
        const status = gt(o, 'status');
        const bc = statusBadge(status);
        const canCancel = status === 'PLACED' || status === 'PAID';
        return `<div class="li">
        <div class="li-left">
          <div class="li-name">Order #${gt(o, 'id')} — 🏪 Restaurant ${gt(o, 'restaurantId')}</div>
          <div class="li-sub">৳${parseFloat(gt(o, 'totalPrice') || 0).toFixed(2)} · ${status.replace(/_/g, ' ')}</div>
        </div>
        <div class="li-right">
          <span class="badge ${bc}">${status.replace(/_/g, ' ')}</span>
          ${canCancel ? `<button class="btn btn-danger btn-sm" onclick="cancelOrder(${gt(o, 'id')})">✕ Cancel</button>` : ''}
        </div>
      </div>`;
      }).join('');
    })
    .catch(() => document.getElementById('order-history').innerHTML = errBox('Failed to load orders.'));
}

/* ── CANCEL ORDER ── */
function cancelOrder(orderId) {
  if (!confirm(`Cancel Order #${orderId}? This cannot be undone.`)) return;
  soap(O_URL, `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:cancelOrder><orderId>${orderId}</orderId></ws:cancelOrder></soapenv:Body></soapenv:Envelope>`)
    .then(() => { loadOrderHistory(); })
    .catch(() => alert('Failed to cancel order.'));
}

/* ── TRACK ── */
const STEPS = ['PLACED', 'PAID', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED'];
const SLABELS = { PLACED: 'Placed', PAID: 'Paid', OUT_FOR_DELIVERY: 'On Way', DELIVERED: 'Delivered', CANCELLED: 'Cancelled' };
const SICONS = { PLACED: '📝', PAID: '💳', OUT_FOR_DELIVERY: '🛵', DELIVERED: '✅', CANCELLED: '✕' };

function trackOrder() {
  const oid = v('orderIdTrack'); const c = document.getElementById('trackResult');
  if (!oid) { c.innerHTML = ''; return; }
  c.innerHTML = `<div style="text-align:center;padding:1.5rem"><div class="spin"></div></div>`;
  soap(O_URL, `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:trackOrder><orderId>${oid}</orderId></ws:trackOrder></soapenv:Body></soapenv:Envelope>`)
    .then(xml => {
      const o = px(xml).getElementsByTagNameNS('*', 'return')[0];
      if (!o) { c.innerHTML = errBox('Order not found.'); return; }
      const status = gt(o, 'status'), total = parseFloat(gt(o, 'totalPrice') || 0).toFixed(2);
      const doneIdx = STEPS.indexOf(status);
      const showSteps = STEPS.filter(s => s !== 'CANCELLED');
      const steps = status === 'CANCELLED'
        ? `<div style="text-align:center;padding:1rem;color:var(--red-err);font-weight:600">✕ This order was cancelled.</div>`
        : showSteps.map((s, i) => `
        <div class="t-step ${i <= doneIdx ? 'done' : ''}">
          <div class="t-dot">${SICONS[s]}</div>
          <div class="t-label">${SLABELS[s]}</div>
        </div>`).join('');
      c.innerHTML = `
      <div style="background:var(--surface2);border:1px solid var(--border);border-radius:10px;padding:1.2rem">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:.9rem">
          <div>
            <div style="font-size:.72rem;color:var(--muted);text-transform:uppercase;letter-spacing:.05em">Order #${oid}</div>
            <div style="font-weight:700;font-size:1rem;margin-top:.2rem;font-family:'Libre Baskerville',serif">৳${total} total</div>
          </div>
          <span class="badge ${statusBadge(status)}">${status.replace(/_/g, ' ')}</span>
        </div>
        <div class="tracker">${steps}</div>
        <div style="margin-top:.9rem;color:var(--muted);font-size:.78rem;display:flex;gap:1.2rem">
          <span>👤 Customer #${gt(o, 'customerId')}</span>
          <span>🏪 Restaurant #${gt(o, 'restaurantId')}</span>
        </div>
      </div>`;
    })
    .catch(() => { c.innerHTML = errBox('Connection error.'); });
}

/* ── REGISTER CUSTOMER ── */
function registerCustomer() {
  const name = v('custName'), phone = v('custPhone'), addr = v('custAddress');
  if (!name || !phone || !addr) return msg('custMsg', 'Please fill all fields.', 'err');
  soap(R_URL, `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:registerCustomer>
      <name>${es(name)}</name><phone>${es(phone)}</phone><address>${es(addr)}</address>
    </ws:registerCustomer></soapenv:Body></soapenv:Envelope>`)
    .then(() => msg('custMsg', '✅ Registered! Use your ID to place orders.', 'ok'))
    .catch(() => msg('custMsg', '❌ Registration failed.', 'err'));
}

/* ── CUSTOMERS ── */
function loadCustomers() {
  loader('customers-list');
  soap(R_URL, `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://wsdl.fooddelivery.com/">
    <soapenv:Body><ws:getAllCustomers/></soapenv:Body></soapenv:Envelope>`)
    .then(xml => {
      const list = px(xml).getElementsByTagNameNS('*', 'return');
      const c = document.getElementById('customers-list');
      if (!list.length) { c.innerHTML = `<div class="empty"><div class="empty-icon">👤</div>No customers yet.</div>`; return; }
      c.innerHTML = Array.from(list).map(cu => `
      <div class="li">
        <div class="li-left">
          <div class="li-name">${gt(cu, 'name')}</div>
          <div class="li-sub">📞 ${gt(cu, 'phone')} · 📍 ${gt(cu, 'address')}</div>
        </div>
        <span class="badge b-gold">ID ${gt(cu, 'id')}</span>
      </div>`).join('');
    });
}

/* ── UTILS ── */
function statusBadge(s) {
  return { PLACED: 'b-gold', PAID: 'b-green', OUT_FOR_DELIVERY: 'b-gold', DELIVERED: 'b-green', CANCELLED: 'b-red' }[s] || 'b-gray';
}
function errBox(t) {
  return `<div class="msg err" style="display:block">❌ ${t}</div>`;
}