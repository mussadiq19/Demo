import { useState } from 'react';
import { useNotifications } from '../hooks/useNotifications';
import NotificationPanel from './notifications/NotificationPanel';

export default function AppLayout({
  pageTitle,
  navItems,
  activeView,
  setActiveView,
  user,
  onLogout,
  children,
}) {
  const [notifOpen, setNotifOpen] = useState(false);
  const { unreadCount } = useNotifications();

  return (
    <div className="flex min-h-screen bg-surface-muted">
      <aside className="fixed left-0 top-0 h-full w-64 bg-brand-900 text-white flex flex-col">
        <div className="px-6 py-5 border-b border-white/10">
          <div className="text-[18px] font-bold text-white">Foresight</div>
          <div className="text-[11px] text-brand-100/60 uppercase tracking-widest">Intelligent Business Operations</div>
        </div>

        <div className="flex-1 overflow-y-auto py-2">
          {['PLATFORM', 'WORKFORCE', 'SETTINGS'].map((section) => (
            <div key={section}>
              <div className="text-[10px] text-brand-100/40 tracking-widest px-6 pt-5 pb-1">{section}</div>
              {navItems.filter((item) => String(item.section || '').toUpperCase() === section).map((item) => (
                <button
                  key={item.id}
                  onClick={() => setActiveView(item.id)}
                  className={`relative flex items-center gap-3 px-4 py-2.5 mx-2 rounded-lg text-sm transition-all w-[calc(100%-1rem)] text-left ${activeView === item.id ? 'bg-white/15 text-white font-medium' : 'text-brand-100/70 hover:bg-white/10 hover:text-white'}`}
                >
                  <span className="w-2 h-2 rounded-full bg-white/40" />
                  {item.label}
                  {item.badge && (
                    <span className="absolute right-3 bg-risk-critical text-white text-xs px-1.5 py-0.5 rounded-full">
                      {item.badge}
                    </span>
                  )}
                </button>
              ))}
            </div>
          ))}
        </div>

        <div className="p-4 border-t border-white/10">
          <div className="flex items-center gap-3 rounded-xl bg-white/5 p-3">
            <div className="w-10 h-10 rounded-full bg-brand-600 flex items-center justify-center font-semibold">
              {user?.fullName?.[0] || user?.email?.[0] || 'U'}
            </div>
            <div className="min-w-0 flex-1">
              <div className="text-sm font-medium truncate">{user?.fullName || 'User'}</div>
              <div className="text-[11px] text-brand-100/50 uppercase tracking-widest truncate">{user?.role || 'Member'}</div>
            </div>
          </div>
          <button
            onClick={onLogout}
            className="mt-3 w-full rounded-xl bg-white/10 px-4 py-2 text-sm font-medium hover:bg-white/15 transition-colors"
          >
            Logout
          </button>
        </div>
      </aside>

      <div className="ml-64 min-h-screen flex-1">
        <header className="sticky top-0 z-10 border-b border-surface-border bg-white/80 backdrop-blur px-8 py-4 flex items-center justify-between">
          <h1 className="text-xl font-semibold text-gray-900">{pageTitle}</h1>
          <div className="flex items-center gap-3">
            <div className="text-xs font-medium px-3 py-1.5 rounded-full border border-brand-100 bg-brand-50 text-brand-600">
              ⚡ Powered by Foresight AI
            </div>
            <div className="relative">
              <button
                onClick={() => setNotifOpen((p) => !p)}
                className="relative p-2 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded-xl transition-colors"
              >
                🔔
                {unreadCount > 0 && (
                  <span className="absolute -top-0.5 -right-0.5 w-4 h-4 bg-risk-critical text-white text-xs rounded-full flex items-center justify-center font-bold">
                    {unreadCount > 9 ? '9+' : unreadCount}
                  </span>
                )}
              </button>
              <NotificationPanel open={notifOpen} onClose={() => setNotifOpen(false)} />
            </div>
            <div className="w-10 h-10 rounded-full bg-brand-600 text-white flex items-center justify-center font-semibold">
              {user?.fullName?.[0] || user?.email?.[0] || 'U'}
            </div>
          </div>
        </header>

        <main className="p-8">{children}</main>
      </div>
    </div>
  );
}
