import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBrokerageClient } from 'app/shared/model/brokerage-client.model';
import { BrokerageClientService } from './brokerage-client.service';

@Component({
    selector: 'jhi-brokerage-client-delete-dialog',
    templateUrl: './brokerage-client-delete-dialog.component.html'
})
export class BrokerageClientDeleteDialogComponent {
    brokerageClient: IBrokerageClient;

    constructor(
        private brokerageClientService: BrokerageClientService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.brokerageClientService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'brokerageClientListModification',
                content: 'Deleted an brokerageClient'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-brokerage-client-delete-popup',
    template: ''
})
export class BrokerageClientDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ brokerageClient }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BrokerageClientDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.brokerageClient = brokerageClient;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
